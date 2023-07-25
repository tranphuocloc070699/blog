const HttpStatusCode = {
  CONTINUE: { code: 100, name: "CONTINUE" },
  OK: { code: 200, name: "OK" },
  CREATED: { code: 201, name: "CREATED" },
  ACCEPTED: { code: 202, name: "ACCEPTED" },
  NO_CONTENT: { code: 204, name: "NO_CONTENT" },
  FOUND: { code: 302, name: "FOUND" },
  BAD_REQUEST: { code: 400, name: "BAD_REQUEST" },
  UNAUTHORIZED: { code: 401, name: "UNAUTHORIZED" },
  FORBIDDEN: { code: 403, name: "FORBIDDEN" },
  NOT_FOUND: { code: 404, name: "NOT_FOUND" },
  REQUEST_TIMEOUT: { code: 408, name: "REQUEST_TIMEOUT" },
  CONFLICT: { code: 409, name: "CONFLICT" },
  UNSUPPORTED_MEDIA_TYPE: { code: 415, name: "UNSUPPORTED_MEDIA_TYPE" },
  INTERNAL_SERVER_ERROR: { code: 500, name: "INTERNAL_SERVER_ERROR" },
  BAD_GATEWAY: { code: 502, name: "BAD_GATEWAY" },
  BANDWIDTH_LIMIT_EXCEEDED: { code: 509, name: "BANDWIDTH_LIMIT_EXCEEDED" },
};

export default HttpStatusCode
