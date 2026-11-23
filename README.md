# SeverlApp_EV_2_DevMov

App Android (Kotlin, Jetpack Compose, Retrofit) con API REST propia en `backend/` (Node.js + Express).

## API

```
cd backend
npm install
npm start
```

Corre en `http://localhost:3000/api`. Recursos: `lineas`, `investigadores`, `proyectos`, `publicaciones`
(GET, GET por id, POST, PUT, PATCH, DELETE). Los datos iniciales están en `backend/data/seed.json`.

## App

- Emulador: `BASE_URL = "http://10.0.2.2:3000/api/"` en `remote/RetrofitInstance.kt`.
- Celular real o servidor desplegado: cambiar `BASE_URL` por la IP o dominio, terminado en `/`.
- Sin conexión, la app usa los datos locales del `Repository`.
