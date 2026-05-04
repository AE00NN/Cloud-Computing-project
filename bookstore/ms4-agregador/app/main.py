import os
import httpx
from fastapi import FastAPI, HTTPException
from fastapi.middleware.cors import CORSMiddleware

MS1_URL = os.getenv("MS1_URL", "http://ms1-catalogo:8001")
MS2_URL = os.getenv("MS2_URL", "http://ms2-pedidos:8002")
MS3_URL = os.getenv("MS3_URL", "http://ms3-resenas:8003")

app = FastAPI(
    title="MS4 - API Agregador BookStore",
    description=(
        "Microservicio agregador: combina datos de Catálogo (MS1), "
        "Pedidos (MS2) y Reseñas (MS3) sin base de datos propia. "
        "BookStore UTEC 2026-1"
    ),
    version="1.0.0",
    docs_url="/docs",
    openapi_url="/openapi.json"
)

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_methods=["*"],
    allow_headers=["*"],
)

# ── helpers ──────────────────────────────────────────────────────────────────
async def get(url: str):
    async with httpx.AsyncClient(timeout=10) as client:
        r = await client.get(url)
        r.raise_for_status()
        return r.json()

# ── endpoints ────────────────────────────────────────────────────────────────

@app.get("/health", tags=["Health"])
def health():
    return {"status": "ok", "service": "ms4-agregador", "version": "1.0.0"}


@app.get(
    "/perfil-cliente/{cliente_id}",
    summary="Perfil completo de un cliente",
    description="Combina datos de cliente (MS2) + sus pedidos (MS2) + sus reseñas (MS3)",
    tags=["Agregados"]
)
async def perfil_cliente(cliente_id: int):
    try:
        cliente = await get(f"{MS2_URL}/clientes/{cliente_id}")
    except Exception:
        raise HTTPException(status_code=404, detail=f"Cliente {cliente_id} no encontrado")

    pedidos, resenas = [], []
    try:
        pedidos = await get(f"{MS2_URL}/pedidos/cliente/{cliente_id}")
    except Exception:
        pass
    try:
        resenas = await get(f"{MS3_URL}/resenas/cliente/{cliente_id}")
    except Exception:
        pass

    return {
        "cliente":         cliente,
        "total_pedidos":   len(pedidos),
        "pedidos":         pedidos,
        "total_resenas":   len(resenas),
        "resenas":         resenas,
    }


@app.get(
    "/detalle-libro/{libro_id}",
    summary="Detalle completo de un libro",
    description="Combina datos del libro (MS1) + sus reseñas (MS3) + rating promedio",
    tags=["Agregados"]
)
async def detalle_libro(libro_id: int):
    try:
        libro = await get(f"{MS1_URL}/libros/{libro_id}")
    except Exception:
        raise HTTPException(status_code=404, detail=f"Libro {libro_id} no encontrado")

    resenas, stats = [], {}
    try:
        resenas = await get(f"{MS3_URL}/resenas/libro/{libro_id}")
    except Exception:
        pass
    try:
        stats = await get(f"{MS3_URL}/resenas/stats/libro/{libro_id}")
    except Exception:
        pass

    return {
        "libro":           libro,
        "rating_promedio": stats.get("promedio_rating", 0),
        "total_resenas":   stats.get("total_resenas", 0),
        "resenas":         resenas,
    }


@app.get(
    "/resumen-pedido/{pedido_id}",
    summary="Resumen enriquecido de un pedido",
    description="Toma un pedido (MS2) y enriquece cada ítem con datos del libro (MS1)",
    tags=["Agregados"]
)
async def resumen_pedido(pedido_id: int):
    try:
        pedido = await get(f"{MS2_URL}/pedidos/{pedido_id}")
    except Exception:
        raise HTTPException(status_code=404, detail=f"Pedido {pedido_id} no encontrado")

    items_enriquecidos = []
    for item in pedido.get("items", []):
        libro_id = item.get("libroId") or item.get("libro_id")
        libro_info = {}
        if libro_id:
            try:
                libro_info = await get(f"{MS1_URL}/libros/{libro_id}")
            except Exception:
                libro_info = {"id": libro_id, "titulo": "No disponible"}
        items_enriquecidos.append({**item, "libro": libro_info})

    return {**pedido, "items": items_enriquecidos}


@app.get(
    "/catalogo-con-stats",
    summary="Catálogo de libros con rating promedio",
    description="Lista libros (MS1) e incorpora rating promedio de reseñas (MS3) para los primeros 10",
    tags=["Agregados"]
)
async def catalogo_con_stats(limit: int = 10):
    try:
        libros = await get(f"{MS1_URL}/libros/?limit={limit}")
    except Exception:
        raise HTTPException(status_code=503, detail="MS1 no disponible")

    resultado = []
    for libro in libros:
        libro_id = libro.get("id")
        stats = {}
        try:
            stats = await get(f"{MS3_URL}/resenas/stats/libro/{libro_id}")
        except Exception:
            pass
        resultado.append({
            **libro,
            "rating_promedio": stats.get("promedio_rating", 0),
            "total_resenas":   stats.get("total_resenas", 0),
        })
    return resultado
