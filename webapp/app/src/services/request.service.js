import { callExternalApi } from "./external-api.service";

const apiServerUrl = process.env.REACT_APP_API_SERVER_URL;

export const createRequest = async (accessToken, title, description) => {
  const config = {
    url: `${apiServerUrl}/api/requests`,
    method: "POST",
    headers: {
      "content-type": "application/json",
      Authorization: `Bearer ${accessToken}`,
    },
    data: {
      "title": title,
      "requestType": {
          "id": 52,
          "typeName": "Help Moving Groceries",
          "typeImageUrl": "https://google.com",
          "insertedOn": "2023-10-08T10:21:42.531028",
          "updatedOn": "2023-10-08T10:21:42.531083"
      },
      "description": description
      }
    };

  const { data, error } = await callExternalApi({ config });

  return {
    data: data || null,
    error,
  };
};

export const getRequests = async (accessToken) => {
  const config = {
    url: `${apiServerUrl}/api/requests`,
    method: "GET",
    headers: {
      "content-type": "application/json",
      Authorization: `Bearer ${accessToken}`,
    }
  };

  const { data, error } = await callExternalApi({ config });

  return {
    data: data || null,
    error,
  };
};
