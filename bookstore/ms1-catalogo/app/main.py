from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from app.routers.libros import router as libros_router
from app.routers.catalogo import autores_router, generos_router, editoriales_router

app = FastAPI(
    title="MS1 - API Catálogo de Libros",
    description="Microservicio de catálogo: libros, autores, géneros y editoriales. BookStore UTEC 2026-1",
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

app.include_router(libros_router)
app.include_router(autores_router)
app.include_router(generos_router)
app.include_router(editoriales_router)

@app.get("/health", tags=["Health"])
def health():
    return {"status": "ok", "service": "ms1-catalogo", "version": "1.0.0"}
