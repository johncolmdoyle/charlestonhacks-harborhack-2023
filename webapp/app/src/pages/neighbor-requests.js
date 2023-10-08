import { useAuth0 } from "@auth0/auth0-react";
import React, { useEffect, useState } from "react";
import { CodeSnippet } from "../components/code-snippet";
import { PageLayout } from "../components/page-layout";
import { getRequests } from "../services/request.service";

export const NeighborRequestsPage = () => {
  const { user } = useAuth0();
  const [requests, setRequests] = useState([]);

  const { getAccessTokenSilently } = useAuth0();

  useEffect(() => {

    if (!user) {
      return null;
    }

    let isMounted = true;
    const requestList = async() => {
      const accessToken = await getAccessTokenSilently();
      const requestList = await getRequests(accessToken);

      if (!isMounted) {
        return;
      }
      
      setRequests(requestList.data);
      
    }

    requestList();

    return () => {
      isMounted = false;
    };
  }, [getAccessTokenSilently]);

  return (
    <PageLayout>
      <div className="content-layout">
        <h1 id="page-title" className="content__title">
          Neighbor Request List
        </h1>
        <div className="content__body">
          <p id="page-description">
            <span>
              List of requests currently open within your distance.
            </span>
          </p>
          <div className="profile-grid">
            <div className="profile__details">
              <table border="1">
                <tr>
                  <td>Type</td>
                  <td>Title</td>
                  <td>Description</td>
                </tr>
                {
                  requests.map((requestItem) => {
                  return (
                    <tr>
                    <td>{requestItem.requestType.typeName}</td>
                    <td>{requestItem.title}</td>
                    <td>{requestItem.description}</td>
                    </tr>
                  )
                })}
              </table>
            </div>
          </div>
        </div>
      </div>
    </PageLayout>
  );
};
