
ARG NODE_VERSION=18-alpine

FROM node:${NODE_VERSION} as base


ENV NODE_ENV=production
# ENV BASE_URL_SERVER=http://host.docker.internal:4000

WORKDIR /app

FROM base as build

COPY  package.json package-lock.json /app/
RUN npm install --production=false

COPY  . .

RUN npm run build
RUN npm prune

# Run
FROM base

ENV HOST 0.0.0.0
ENV PORT 3000
EXPOSE 3000

COPY --from=build /app/.output /app/.output

CMD [ "node", ".output/server/index.mjs" ]