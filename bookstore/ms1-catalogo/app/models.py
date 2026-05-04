from sqlalchemy import Column, Integer, String, Float, Text, Date
from app.database import Base

class Libro(Base):
    __tablename__ = "libros"
    id             = Column(Integer, primary_key=True, index=True)
    titulo         = Column(String(200), nullable=False)
    autor_id       = Column(Integer, nullable=False)
    editorial_id   = Column(Integer, nullable=False)
    genero_id      = Column(Integer, nullable=False)
    isbn           = Column(String(20), unique=True)
    precio         = Column(Float, nullable=False)
    stock          = Column(Integer, default=0)
    año_publicacion = Column(Integer)
    paginas        = Column(Integer)
    idioma         = Column(String(40), default="Español")

class Autor(Base):
    __tablename__ = "autores"
    id              = Column(Integer, primary_key=True, index=True)
    nombre          = Column(String(120), nullable=False)
    nacionalidad    = Column(String(60))
    fecha_nacimiento = Column(Date)
    biografia       = Column(Text)

class Genero(Base):
    __tablename__ = "generos"
    id          = Column(Integer, primary_key=True, index=True)
    nombre      = Column(String(80), nullable=False, unique=True)
    descripcion = Column(Text)

class Editorial(Base):
    __tablename__ = "editoriales"
    id             = Column(Integer, primary_key=True, index=True)
    nombre         = Column(String(120), nullable=False)
    pais           = Column(String(60))
    año_fundacion  = Column(Integer)
    email_contacto = Column(String(120))
    sitio_web      = Column(String(200))
