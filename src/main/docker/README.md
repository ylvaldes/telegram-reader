## Crear y actualizar docker

Ejemplo de crear  el docker

```bash
docker build -f .\src\main\docker\Dockerfile -t ylvaldes-telegram-reader .
```
### Levantar la imagen desde la local

```bash
docker run -d --name ylvaldes-telegram-reader  -p 8187:8187 -t -i ylvaldes-telegram-reader
```
