require('dotenv').config();
const express  = require('express');
const mongoose = require('mongoose');
const cors     = require('cors');
const swaggerUi = require('swagger-ui-express');

const resenasRoutes = require('./routes/resenas.routes');

const app  = express();
const PORT = process.env.PORT || 8003;
const MONGO_URI = process.env.MONGO_URI || 'mongodb://172.31.44.253:27017/bookstore';

// Middleware
app.use(cors());
app.use(express.json());

// Swagger docs inline (sin YAML externo)
const swaggerDoc = {
  openapi: '3.0.0',
  info: {
    title: 'MS3 - API Reseñas BookStore',
    version: '1.0.0',
    description: 'Microservicio de reseñas y ratings de libros (MongoDB + Node.js Express). BookStore UTEC 2026-1'
  },
  servers: [{ url: `http://localhost:${PORT}` }],
  paths: {
    '/resenas': {
      get: { summary: 'Listar reseñas con filtros', tags: ['Reseñas'],
        parameters: [
          { name: 'libro_id',   in: 'query', schema: { type: 'integer' } },
          { name: 'cliente_id', in: 'query', schema: { type: 'integer' } },
          { name: 'rating',     in: 'query', schema: { type: 'integer' } },
          { name: 'limit',      in: 'query', schema: { type: 'integer', default: 20 } },
          { name: 'skip',       in: 'query', schema: { type: 'integer', default: 0 } }
        ],
        responses: { 200: { description: 'Lista de reseñas' } }
      },
      post: { summary: 'Crear reseña', tags: ['Reseñas'],
        requestBody: { required: true, content: { 'application/json': { schema: {
          type: 'object',
          properties: {
            libro_id: { type: 'integer' }, cliente_id: { type: 'integer' },
            rating: { type: 'integer' }, titulo: { type: 'string' },
            comentario: { type: 'string' }
          }
        }}}},
        responses: { 201: { description: 'Reseña creada' } }
      }
    },
    '/resenas/libro/{libro_id}': {
      get: { summary: 'Reseñas por libro', tags: ['Reseñas'],
        parameters: [{ name: 'libro_id', in: 'path', required: true, schema: { type: 'integer' } }],
        responses: { 200: { description: 'Lista de reseñas del libro' } }
      }
    },
    '/resenas/cliente/{cliente_id}': {
      get: { summary: 'Reseñas por cliente', tags: ['Reseñas'],
        parameters: [{ name: 'cliente_id', in: 'path', required: true, schema: { type: 'integer' } }],
        responses: { 200: { description: 'Lista de reseñas del cliente' } }
      }
    },
    '/resenas/stats/libro/{libro_id}': {
      get: { summary: 'Rating promedio de un libro', tags: ['Estadísticas'],
        parameters: [{ name: 'libro_id', in: 'path', required: true, schema: { type: 'integer' } }],
        responses: { 200: { description: 'Rating promedio y total reseñas' } }
      }
    },
    '/resenas/{id}': {
      get: { summary: 'Obtener reseña por ID', tags: ['Reseñas'],
        parameters: [{ name: 'id', in: 'path', required: true, schema: { type: 'string' } }],
        responses: { 200: { description: 'Reseña' }, 404: { description: 'No encontrada' } }
      },
      patch: { summary: 'Actualizar reseña', tags: ['Reseñas'],
        parameters: [{ name: 'id', in: 'path', required: true, schema: { type: 'string' } }],
        requestBody: { required: true, content: { 'application/json': { schema: { type: 'object' } } } },
        responses: { 200: { description: 'Reseña actualizada' } }
      },
      delete: { summary: 'Eliminar reseña', tags: ['Reseñas'],
        parameters: [{ name: 'id', in: 'path', required: true, schema: { type: 'string' } }],
        responses: { 204: { description: 'Eliminada' } }
      }
    },
    '/health': {
      get: { summary: 'Health check', tags: ['Health'],
        responses: { 200: { description: 'OK' } }
      }
    }
  }
};

app.use('/docs', swaggerUi.serve, swaggerUi.setup(swaggerDoc));
app.get('/openapi.json', (req, res) => res.json(swaggerDoc));

// Routes
app.use('/resenas', resenasRoutes);
app.get('/health', (req, res) => res.json({ status: 'ok', service: 'ms3-resenas', version: '1.0.0' }));

// Conectar MongoDB y arrancar
mongoose.connect(MONGO_URI)
  .then(() => {
    console.log('✅ MongoDB conectado:', MONGO_URI);
    app.listen(PORT, '0.0.0.0', () => {
      console.log(`🚀 MS3-Reseñas corriendo en puerto ${PORT}`);
      console.log(`📚 Swagger: http://localhost:${PORT}/docs`);
    });
  })
  .catch(err => {
    console.error('❌ Error conectando MongoDB:', err.message);
    process.exit(1);
  });
