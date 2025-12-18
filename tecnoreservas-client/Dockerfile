# Stage 1: Build
FROM node:18-alpine AS build
WORKDIR /app
COPY package*.json ./
RUN npm ci --silent
COPY . .
RUN npm run build

# Stage 2: Production con nginx
FROM nginx:alpine
WORKDIR /usr/share/nginx/html

# Eliminar archivos por defecto de nginx
RUN rm -rf ./*

# Copiar los archivos build
COPY --from=build /app/dist .

# Copiar configuración de nginx para SPA
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Exponer puerto 80
EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]