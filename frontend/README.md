# DsCommerce frontend

Vue 3 + Vue Router + Vite storefront for the Spring Boot API in the repository root.

## Run locally

1. Development and production Vite modes use `https://dscommerce-fyii.onrender.com` as the API base URL.
2. Run `npm install`, then `npm run dev`.
3. Open <http://localhost:5173>.

To use a local backend instead, create `.env.local` with `VITE_API_BASE_URL=http://localhost:8080`. Vite loads `.env.local` in both development and production and it is excluded from Git.

Run `npm run build` to create the production bundle in `dist/`.

## API contract used

- `GET /products?name=&page=&size=` returns the Spring Data page of `ProductMinDTO` values.
- `GET /products/{id}` returns product details, categories and the `imgUrl` stored by the API.
- `GET /categories` returns the category list.
- `POST /oauth2/token` uses the API's password grant and HTTP Basic client authentication.
- `GET /users/me` supplies the signed-in user's profile and roles.
- `POST /orders` creates an authenticated client order; the API resolves current product prices.

The API does not expose a category-filter parameter, a product gallery, shipping calculation, payment processing, or an account order-list endpoint. The interface only displays categories and product information present in the API. The basket is stored in the browser until an authenticated checkout submits an order.

## Authentication deployment note

This backend requires a client secret for its token endpoint. Values prefixed with `VITE_` are included in browser-delivered code, so a secret in this Vue app cannot be kept confidential. The provided credentials are the backend's local defaults for development only. A public-client flow such as Authorization Code with PKCE, or a server-side proxy that keeps the secret, is needed before exposing this frontend publicly. The backend was left unchanged as requested.

The backend's CORS configuration must include the frontend's origin. Its current defaults include `http://localhost:5173` and `http://localhost:5174`; if the frontend is hosted on another domain, add that exact origin to the Render `CORS_ORIGINS` environment variable.
