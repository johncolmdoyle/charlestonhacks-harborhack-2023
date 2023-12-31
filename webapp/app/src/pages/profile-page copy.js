import { useAuth0 } from "@auth0/auth0-react";
import React, {useState} from "react";
import { CodeSnippet } from "../components/code-snippet";
import { PageLayout } from "../components/page-layout";

export const ProfilePage = () => {
  const { user } = useAuth0();
  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [phoneNumber, setPhoneNumber] = useState("");
  const [email, setEmail] = useState("");
  const [addressLine1, setAddressLine1] = useState("");
  const [addressLine2, setAddressLine2] = useState("");
  const [addressCity, setAddressCity] = useState("");
  const [addressState, setAddressState] = useState("");
  const [addressZipCode, setAddressZipCode] = useState("");
  const [message, setMessage] = useState("");

  if (!user) {
    return null;
  }

  let handleSubmit = async (e) => {
    e.preventDefault();
    try {
      let res = await fetch("https://api.chucktownneighbors.com/api/users/502", {
        method: "POST",
        body: JSON.stringify({
          firstName: firstName,
          lastName: lastName,
          phoneNumber: phoneNumber,
          email: email,
          address: {
            line1: addressLine1,
            line2: addressLine2,
            city: addressCity,
            state: addressState,
            zipCode: addressZipCode
          }
        }),
      });
      let resJson = await res.json();
      if (res.status === 200) {
        setMessage("User updated successfully");
      } else {
        setMessage("Some error occured");
      }
    } catch (err) {
      console.log(err);
    }
  };

  return (
    <PageLayout>
      <div className="content-layout">
        <h1 id="page-title" className="content__title">
          Profile Page
        </h1>
        <div className="content__body">
          <p id="page-description">
            <span>
              You can use the <strong>ID Token</strong> to get the profile
              information of an authenticated user.
            </span>
            <span>
              <strong>Only authenticated users can access this page.</strong>
            </span>
          </p>
          <div className="profile-grid">
            <div className="profile__header">
              <img
                src={user.picture}
                alt="Profile"
                className="profile__avatar"
              />
              <div className="profile__headline">
                <h2 className="profile__title">{user.name}</h2>
                <span className="profile__description">{user.email}</span>
              </div>
            </div>
            <div className="profile__details">
              <CodeSnippet
                title="Decoded ID Token"
                code={JSON.stringify(user, null, 2)}
              />
            </div>
          </div>
        </div>
      </div>
    </PageLayout>
  );
};
