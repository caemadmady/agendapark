FROM node:18-alpine AS build
WORKDIR /app
COPY package*.json ./
RUN npm ci --silent
COPY . .
RUN npm run build

FROM alpine:3.19
WORKDIR /app
COPY --from=build /app/dist /app/dist