import { useAuth0 } from "@auth0/auth0-react";
import React, { useEffect, useState } from "react";
import { CodeSnippet } from "../components/code-snippet";
import { PageLayout } from "../components/page-layout";
import { createRequest } from "../services/request.service";

export const OpenRequestPage = () => {
  const { user } = useAuth0();
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");

  const { getAccessTokenSilently } = useAuth0();  

  //setEmail(user.email);

  let handleSubmit = async (e) => {
    e.preventDefault();
    const accessToken = await getAccessTokenSilently();
    await createRequest(accessToken, title, description);
  };  

  return (
    <PageLayout>
      <div className="content-layout">
        <h1 id="page-title" className="content__title">
          Open Request
        </h1>
        <div className="content__body">
          <p id="page-description">
            <span>
              You can open a request to help <strong>Move Groceries</strong> currently.
            </span>
          </p>
          <div className="profile-grid">
            <div className="profile__header">
              <img
                src="https://www.bankrate.com/2020/08/01170557/How-to-save-money-on-groceries.jpeg"
                alt="Groceries"
                className="profile__avatar"
              />
              <div className="profile__headline">
                <h2 className="profile__title">Help Moving Groceries</h2>
              </div>
            </div>
            <div className="profile__details">
              <form onSubmit={handleSubmit}>
                <p id="page-description">
                  <span>
                    Title
                  </span>
                  <span>
                    <input
                      type="text"
                      value={title}
                      placeholder="Title"
                      onChange={(e) => setTitle(e.target.value)}
                    />
                  </span>
                </p>

                <p id="page-description">
                  <span>
                    Description
                  </span>
                  <span>
                    <input
                      type="text"
                      value={description}
                      placeholder="Description"
                      onChange={(e) => setDescription(e.target.value)}
                    />
                  </span>
                </p>

                <button type="submit">Create Request</button>
              </form>
            </div>
          </div>
        </div>
      </div>
    </PageLayout>
  );
};
