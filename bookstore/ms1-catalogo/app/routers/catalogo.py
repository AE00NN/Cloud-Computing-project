from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session
from app.database import get_db
from app.models import Autor, Genero, Editorial

# ── Autores ──────────────────────────────────────────────────────────────────
autores_router = APIRouter(prefix="/autores", tags=["Autores"])

@autores_router.get("/", summary="Listar autores")
def listar_autores(skip: int = 0, limit: int = 20, db: Session = Depends(get_db)):
    return db.query(Autor).offset(skip).limit(limit).all()

@autores_router.get("/{autor_id}", summary="Obtener autor por ID")
def obtener_autor(autor_id: int, db: Session = Depends(get_db)):
    autor = db.query(Autor).filter(Autor.id == autor_id).first()
    if not autor:
        raise HTTPException(status_code=404, detail="Autor no encontrado")
    return autor

@autores_router.post("/", status_code=201, summary="Crear autor")
def crear_autor(data: dict, db: Session = Depends(get_db)):
    autor = Autor(**data)
    db.add(autor)
    db.commit()
    db.refresh(autor)
    return autor

# ── Géneros ───────────────────────────────────────────────────────────────────
generos_router = APIRouter(prefix="/generos", tags=["Géneros"])

@generos_router.get("/", summary="Listar géneros")
def listar_generos(db: Session = Depends(get_db)):
    return db.query(Genero).all()

@generos_router.get("/{genero_id}", summary="Obtener género por ID")
def obtener_genero(genero_id: int, db: Session = Depends(get_db)):
    genero = db.query(Genero).filter(Genero.id == genero_id).first()
    if not genero:
        raise HTTPException(status_code=404, detail="Género no encontrado")
    return genero

# ── Editoriales ───────────────────────────────────────────────────────────────
editoriales_router = APIRouter(prefix="/editoriales", tags=["Editoriales"])

@editoriales_router.get("/", summary="Listar editoriales")
def listar_editoriales(skip: int = 0, limit: int = 20, db: Session = Depends(get_db)):
    return db.query(Editorial).offset(skip).limit(limit).all()

@editoriales_router.get("/{editorial_id}", summary="Obtener editorial por ID")
def obtener_editorial(editorial_id: int, db: Session = Depends(get_db)):
    editorial = db.query(Editorial).filter(Editorial.id == editorial_id).first()
    if not editorial:
        raise HTTPException(status_code=404, detail="Editorial no encontrada")
    return editorial
