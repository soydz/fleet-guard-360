import { useNavigate } from "react-router-dom";

const API_URL = "/api";

interface GraphQLResponse<T> {
  data?: T;
  errors?: Array<{ message: string }>;
}

export const useGraphQL = () => {
  const navigate = useNavigate();

  const fetchGraphQL = async <T>(
    endpoint: string,
    query: string,
    variables?: Record<string, unknown>
  ): Promise<T> => {
    const token = localStorage.getItem("jwt_token") || sessionStorage.getItem("jwt_token");
    
    if (!token) {
      navigate("/");
      throw new Error("No authentication token found");
    }

    const response = await fetch(`${API_URL}${endpoint}`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify({ query, variables }),
    });

    if (response.status === 401 || response.status === 403) {
      localStorage.removeItem("jwt_token");
      sessionStorage.removeItem("jwt_token");
      navigate("/", { state: { sessionExpired: true } });
      throw new Error("Session expired");
    }

    const result: GraphQLResponse<T> = await response.json();

    if (result.errors) {
      throw new Error(result.errors[0]?.message || "GraphQL error");
    }

    if (!result.data) {
      throw new Error("No data returned from GraphQL");
    }

    return result.data;
  };

  return { fetchGraphQL };
};
