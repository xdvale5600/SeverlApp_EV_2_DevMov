const express = require("express");
const cors = require("cors");
const fs = require("fs");
const path = require("path");

const SEED_FILE = path.join(__dirname, "data", "seed.json");
const DB_FILE = path.join(__dirname, "data", "db.json");

if (!fs.existsSync(DB_FILE)) {
  fs.copyFileSync(SEED_FILE, DB_FILE);
}

const leer = () => JSON.parse(fs.readFileSync(DB_FILE, "utf-8"));
const guardar = (db) => fs.writeFileSync(DB_FILE, JSON.stringify(db, null, 2));

const esquemas = {
  lineas: { titulo: "string", descripcion: "string" },
  investigadores: { idLinea: "number", nombre: "string", especialidad: "string" },
  proyectos: { idInvestigador: "number", titulo: "string", anio: "number", estado: "string" },
  publicaciones: {
    idProyecto: "number",
    titulo: "string",
    autor: "string",
    anio: "number",
    requiereToken: "boolean"
  }
};

const validar = (esquema, cuerpo, parcial) => {
  const datos = {};
  for (const [campo, tipo] of Object.entries(esquema)) {
    if (cuerpo[campo] === undefined) {
      if (!parcial) return { error: `Falta el campo ${campo}` };
      continue;
    }
    if (typeof cuerpo[campo] !== tipo) {
      return { error: `El campo ${campo} debe ser ${tipo}` };
    }
    datos[campo] = cuerpo[campo];
  }
  return { datos };
};

const app = express();
app.use(cors());
app.use(express.json());

app.get("/api", (req, res) => {
  res.json({ recursos: Object.keys(esquemas) });
});

for (const [recurso, esquema] of Object.entries(esquemas)) {
  const ruta = `/api/${recurso}`;

  app.get(ruta, (req, res) => {
    let lista = leer()[recurso];
    for (const campo of Object.keys(esquema)) {
      if (req.query[campo] !== undefined) {
        lista = lista.filter((item) => String(item[campo]) === String(req.query[campo]));
      }
    }
    res.json(lista);
  });

  app.get(`${ruta}/:id`, (req, res) => {
    const item = leer()[recurso].find((x) => x.id === Number(req.params.id));
    if (!item) return res.status(404).json({ error: "No encontrado" });
    res.json(item);
  });

  app.post(ruta, (req, res) => {
    const { datos, error } = validar(esquema, req.body, false);
    if (error) return res.status(400).json({ error });
    const db = leer();
    const nuevoId = db[recurso].reduce((max, x) => Math.max(max, x.id), 0) + 1;
    const item = { id: nuevoId, ...datos };
    db[recurso].push(item);
    guardar(db);
    res.status(201).json(item);
  });

  const actualizar = (parcial) => (req, res) => {
    const { datos, error } = validar(esquema, req.body, parcial);
    if (error) return res.status(400).json({ error });
    const db = leer();
    const indice = db[recurso].findIndex((x) => x.id === Number(req.params.id));
    if (indice === -1) return res.status(404).json({ error: "No encontrado" });
    db[recurso][indice] = { id: db[recurso][indice].id, ...(parcial ? db[recurso][indice] : {}), ...datos };
    guardar(db);
    res.json(db[recurso][indice]);
  };

  app.put(`${ruta}/:id`, actualizar(false));
  app.patch(`${ruta}/:id`, actualizar(true));

  app.delete(`${ruta}/:id`, (req, res) => {
    const db = leer();
    const indice = db[recurso].findIndex((x) => x.id === Number(req.params.id));
    if (indice === -1) return res.status(404).json({ error: "No encontrado" });
    db[recurso].splice(indice, 1);
    guardar(db);
    res.status(204).end();
  });
}

const PORT = process.env.PORT || 3000;
app.listen(PORT, "0.0.0.0", () => {
  console.log(`API escuchando en el puerto ${PORT}`);
});
