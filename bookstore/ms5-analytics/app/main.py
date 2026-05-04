from fastapi import FastAPI, Query
from fastapi.middleware.cors import CORSMiddleware
from app.athena import run_query

app = FastAPI(
    title="MS5 - API Analytics BookStore",
    description=(
        "Microservicio analítico: ejecuta consultas SQL sobre AWS Athena "
        "(catálogo bookstore_catalog) usando el Data Lake en S3. "
        "Incluye las vistas vista_ventas_por_genero y vista_autores_top. "
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

@app.get("/health", tags=["Health"])
def health():
    return {"status": "ok", "service": "ms5-analytics", "version": "1.0.0"}


@app.get(
    "/ventas-por-genero",
    summary="Ventas e ingresos agrupados por género literario",
    description="Usa la vista `vista_ventas_por_genero` de Athena (bookstore_catalog).",
    tags=["Analytics"]
)
def ventas_por_genero():
    sql = """
        SELECT *
        FROM bookstore_catalog.vista_ventas_por_genero
        ORDER BY ingresos_total DESC
    """
    return run_query(sql)


@app.get(
    "/top-autores",
    summary="Top autores por ingresos generados",
    description="Usa la vista `vista_autores_top` de Athena.",
    tags=["Analytics"]
)
def top_autores(limit: int = Query(default=10, ge=1, le=50)):
    sql = f"""
        SELECT *
        FROM bookstore_catalog.vista_autores_top
        LIMIT {limit}
    """
    return run_query(sql)


@app.get(
    "/top-clientes",
    summary="Top clientes por gasto total",
    description="Consulta clientes con mayor número de pedidos e ingresos (cross MS1+MS2).",
    tags=["Analytics"]
)
def top_clientes(limit: int = Query(default=20, ge=1, le=100)):
    sql = f"""
        SELECT
            c.nombre AS cliente,
            c.ciudad,
            c.pais,
            COUNT(p.id) AS num_pedidos,
            ROUND(SUM(CAST(p.total AS DOUBLE)), 2) AS gasto_total
        FROM bookstore_catalog.pedidos p
        JOIN bookstore_catalog.clientes c ON p.cliente_id = c.id
        GROUP BY c.nombre, c.ciudad, c.pais
        ORDER BY gasto_total DESC
        LIMIT {limit}
    """
    return run_query(sql)


@app.get(
    "/rating-por-genero",
    summary="Rating promedio de reseñas agrupado por género",
    description="Une reseñas (MS3), libros y géneros (MS1) para calcular rating promedio por género.",
    tags=["Analytics"]
)
def rating_por_genero():
    sql = """
        SELECT
            g.nombre AS genero,
            ROUND(AVG(CAST(r.rating AS DOUBLE)), 2) AS rating_promedio,
            COUNT(r.libro_id) AS total_resenas
        FROM bookstore_catalog.resenas r
        JOIN bookstore_catalog.libros l ON r.libro_id = l.id
        JOIN bookstore_catalog.generos g ON l.genero_id = g.id
        GROUP BY g.nombre
        ORDER BY rating_promedio DESC
    """
    return run_query(sql)


@app.get(
    "/libros-mas-vendidos",
    summary="Libros más vendidos por unidades",
    description="Calcula unidades vendidas por libro cruzando detalle_pedido con catálogo.",
    tags=["Analytics"]
)
def libros_mas_vendidos(limit: int = Query(default=10, ge=1, le=50)):
    sql = f"""
        SELECT
            l.titulo,
            a.nombre AS autor,
            g.nombre AS genero,
            SUM(CAST(dp.cantidad AS INT)) AS unidades_vendidas,
            ROUND(SUM(CAST(dp.cantidad AS INT) * CAST(dp.precio_unitario AS DOUBLE)), 2) AS ingresos
        FROM bookstore_catalog.detalle_pedido dp
        JOIN bookstore_catalog.libros l    ON dp.libro_id   = l.id
        JOIN bookstore_catalog.autores a   ON l.autor_id    = a.id
        JOIN bookstore_catalog.generos g   ON l.genero_id   = g.id
        GROUP BY l.titulo, a.nombre, g.nombre
        ORDER BY unidades_vendidas DESC
        LIMIT {limit}
    """
    return run_query(sql)
