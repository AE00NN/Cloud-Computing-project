const mongoose = require('mongoose');

const resenaSchema = new mongoose.Schema({
  libro_id:     { type: Number, required: true, index: true },
  cliente_id:   { type: Number, required: true, index: true },
  rating:       { type: Number, required: true, min: 1, max: 5 },
  titulo:       { type: String },
  comentario:   { type: String },
  fecha:        { type: String },
  votos_utiles: { type: Number, default: 0 },
  verificado:   { type: Boolean, default: false },
  tags:         { type: [String], default: [] },
  idioma:       { type: String, default: 'es' }
}, {
  collection: 'resenas',   // colección exacta en MongoDB
  versionKey: false
});

module.exports = mongoose.model('Resena', resenaSchema);
