FROM node:26-alpine
WORKDIR /usr/src/app
# Copy package files first (better caching for builds)
COPY package*.json ./
RUN npm install
COPY . .
EXPOSE 3000
CMD [ "node", "app.js" ]
