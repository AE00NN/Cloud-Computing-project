from fastapi import APIRouter, Depends, HTTPException, Query
from sqlalchemy.orm import Session
from sqlalchemy import or_
from typing import Optional, List
from app.database import get_db
from app.models import Libro

router = APIRouter(prefix="/libros", tags=["Libros"])

@router.get("/", summary="Listar libros con filtros opcionales")
def listar_libros(
    autor_id:   Optional[int]   = Query(None),
    genero_id:  Optional[int]   = Query(None),
    min_precio: Optional[float] = Query(None),
    max_precio: Optional[float] = Query(None),
    skip: int = 0,
    limit: int = 20,
    db: Session = Depends(get_db)
):
    q = db.query(Libro)
    if autor_id:   q = q.filter(Libro.autor_id   == autor_id)
    if genero_id:  q = q.filter(Libro.genero_id  == genero_id)
    if min_precio: q = q.filter(Libro.precio     >= min_precio)
    if max_precio: q = q.filter(Libro.precio     <= max_precio)
    return q.offset(skip).limit(limit).all()

@router.get("/search", summary="Buscar libros por título")
def buscar_libros(q: str, db: Session = Depends(get_db)):
    return db.query(Libro).filter(Libro.titulo.ilike(f"%{q}%")).limit(50).all()

@router.get("/{libro_id}", summary="Obtener libro por ID")
def obtener_libro(libro_id: int, db: Session = Depends(get_db)):
    libro = db.query(Libro).filter(Libro.id == libro_id).first()
    if not libro:
        raise HTTPException(status_code=404, detail="Libro no encontrado")
    return libro

@router.post("/", status_code=201, summary="Crear libro")
def crear_libro(data: dict, db: Session = Depends(get_db)):
    libro = Libro(**data)
    db.add(libro)
    db.commit()
    db.refresh(libro)
    return libro

@router.patch("/{libro_id}", summary="Actualizar libro parcialmente")
def actualizar_libro(libro_id: int, data: dict, db: Session = Depends(get_db)):
    libro = db.query(Libro).filter(Libro.id == libro_id).first()
    if not libro:
        raise HTTPException(status_code=404, detail="Libro no encontrado")
    for key, value in data.items():
        if hasattr(libro, key) and value is not None:
            setattr(libro, key, value)
    db.commit()
    db.refresh(libro)
    return libro

@router.delete("/{libro_id}", status_code=204, summary="Eliminar libro")
def eliminar_libro(libro_id: int, db: Session = Depends(get_db)):
    libro = db.query(Libro).filter(Libro.id == libro_id).first()
    if not libro:
        raise HTTPException(status_code=404, detail="Libro no encontrado")
    db.delete(libro)
    db.commit()
