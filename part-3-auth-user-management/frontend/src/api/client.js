const BASE = "http://localhost:8080";
export async function request(path, { token, method = "GET", body } = {}) {
  const response = await fetch(BASE + path, {
    method,
    headers: {
      "Content-Type": "application/json",
      ...(token ? { Authorization: `Bearer ${token}` } : {}),
    },
    ...(body ? { body: JSON.stringify(body) } : {}),
  });
  const data = await response
    .json()
    .catch(() => ({ message: "Unexpected server response" }));
  if (!response.ok) throw new Error(data.message || `HTTP ${response.status}`);
  return data;
}
