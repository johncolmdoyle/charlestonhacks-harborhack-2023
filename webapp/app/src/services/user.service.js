import { callExternalApi } from "./external-api.service";

const apiServerUrl = process.env.REACT_APP_API_SERVER_URL;

export const createClient = async (accessToken) => {
  const config = {
    url: `${apiServerUrl}/api/users`,
    method: "POST",
    headers: {
      "content-type": "application/json",
      Authorization: `Bearer ${accessToken}`,
    },
    data: {
      "firstName": "",
      "lastName": "",
      "phoneNumber": "",
      "maxDistance": 1.0,
      "address": {
        "line1": "",
        "line2": "",
        "city": "",
        "state": "",
        "zipCode": ""
      }
    }
  };

  const { data, error } = await callExternalApi({ config });

  return {
    data: data || null,
    error,
  };
};

export const putClient = async (accessToken, id, firstName, lastName, phoneNumber, maxDistance, line1, line2, city, state, zipCode) => {
  const config = {
    url: `${apiServerUrl}/api/users/${id}`,
    method: "PUT",
    headers: {
      "content-type": "application/json",
      Authorization: `Bearer ${accessToken}`,
    },
    data: {
      "firstName": firstName,
      "lastName": lastName,
      "phoneNumber": phoneNumber,
      "maxDistance": maxDistance,
      "address": {
        "line1": line1,
        "line2": line2,
        "city": city,
        "state": state,
        "zipCode": zipCode
      }
    }
  };

  const { data, error } = await callExternalApi({ config });

  return {
    data: data || null,
    error,
  };
};

export const getCurrentClient = async (accessToken) => {
  const config = {
    url: `${apiServerUrl}/api/users/me`,
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
