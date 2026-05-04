const express = require('express');
const router  = express.Router();
const Resena  = require('../models/resena.model');

// GET /resenas — listar con filtros opcionales
router.get('/', async (req, res) => {
  try {
    const { libro_id, cliente_id, rating, limit = 20, skip = 0 } = req.query;
    const filtro = {};
    if (libro_id)   filtro.libro_id   = parseInt(libro_id);
    if (cliente_id) filtro.cliente_id = parseInt(cliente_id);
    if (rating)     filtro.rating     = parseInt(rating);

    const resenas = await Resena.find(filtro)
      .skip(parseInt(skip))
      .limit(parseInt(limit));
    res.json(resenas);
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

// GET /resenas/libro/:libro_id — reseñas de un libro
router.get('/libro/:libro_id', async (req, res) => {
  try {
    const resenas = await Resena.find({ libro_id: parseInt(req.params.libro_id) });
    res.json(resenas);
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

// GET /resenas/cliente/:cliente_id — reseñas de un cliente
router.get('/cliente/:cliente_id', async (req, res) => {
  try {
    const resenas = await Resena.find({ cliente_id: parseInt(req.params.cliente_id) });
    res.json(resenas);
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

// GET /resenas/:id — una reseña por _id
router.get('/:id', async (req, res) => {
  try {
    const resena = await Resena.findById(req.params.id);
    if (!resena) return res.status(404).json({ error: 'Reseña no encontrada' });
    res.json(resena);
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

// POST /resenas — crear reseña
router.post('/', async (req, res) => {
  try {
    const resena = new Resena({
      ...req.body,
      fecha: req.body.fecha || new Date().toISOString()
    });
    await resena.save();
    res.status(201).json(resena);
  } catch (err) {
    res.status(400).json({ error: err.message });
  }
});

// PATCH /resenas/:id — actualizar reseña
router.patch('/:id', async (req, res) => {
  try {
    const resena = await Resena.findByIdAndUpdate(
      req.params.id,
      { $set: req.body },
      { new: true }
    );
    if (!resena) return res.status(404).json({ error: 'Reseña no encontrada' });
    res.json(resena);
  } catch (err) {
    res.status(400).json({ error: err.message });
  }
});

// DELETE /resenas/:id — eliminar reseña
router.delete('/:id', async (req, res) => {
  try {
    const resena = await Resena.findByIdAndDelete(req.params.id);
    if (!resena) return res.status(404).json({ error: 'Reseña no encontrada' });
    res.status(204).send();
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

// GET /resenas/stats/libro/:libro_id — rating promedio de un libro
router.get('/stats/libro/:libro_id', async (req, res) => {
  try {
    const stats = await Resena.aggregate([
      { $match: { libro_id: parseInt(req.params.libro_id) } },
      { $group: {
        _id: '$libro_id',
        promedio_rating: { $avg: '$rating' },
        total_resenas:   { $sum: 1 }
      }}
    ]);
    res.json(stats[0] || { libro_id: req.params.libro_id, promedio_rating: 0, total_resenas: 0 });
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

module.exports = router;
