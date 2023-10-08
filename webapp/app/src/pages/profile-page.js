import { useAuth0 } from "@auth0/auth0-react";
import React, { useEffect, useState } from "react";
import { CodeSnippet } from "../components/code-snippet";
import { PageLayout } from "../components/page-layout";
import { getCurrentClient, putClient } from "../services/user.service";

export const ProfilePage = () => {
  const { user } = useAuth0();
  const [clientId, setClientId] = useState("");
  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [phoneNumber, setPhoneNumber] = useState("");
  const [email, setEmail] = useState("");
  const [addressLine1, setAddressLine1] = useState("");
  const [addressLine2, setAddressLine2] = useState("");
  const [addressCity, setAddressCity] = useState("");
  const [addressState, setAddressState] = useState("");
  const [addressZipCode, setAddressZipCode] = useState("");
  const [maxDistance, setMaxDistance] = useState(1);
  const [message, setMessage] = useState("");

  const { getAccessTokenSilently } = useAuth0();

  useEffect(() => {

    if (!user) {
      return null;
    }

    let isMounted = true;
    const currentUser = async() => {
      const accessToken = await getAccessTokenSilently();
      const currentClient = await getCurrentClient(accessToken);

      if (!isMounted) {
        return;
      }
      
      //setClient(currentClient);
      setClientId(currentClient.data.id);
      setFirstName(currentClient.data.firstName);
      setLastName(currentClient.data.lastName);
      setPhoneNumber(currentClient.data.phoneNumber);
      setEmail(currentClient.data.email);

      if (currentClient.data.address != null) {
        setAddressLine1(currentClient.data.address.line1);
        setAddressLine2(currentClient.data.address.line2);
        setAddressCity(currentClient.data.address.city);
        setAddressState(currentClient.data.address.state);
        setAddressZipCode(currentClient.data.address.zipCode);
      }

      setMaxDistance(currentClient.data.maxDistance);
      
    }

    currentUser();

    return () => {
      isMounted = false;
    };
  }, [getAccessTokenSilently]);
  

  //setEmail(user.email);

  let handleSubmit = async (e) => {
    e.preventDefault();
    const accessToken = await getAccessTokenSilently();
    await putClient(accessToken, clientId, firstName, lastName, phoneNumber, maxDistance, addressLine1, addressLine2, addressCity, addressState, addressZipCode);
  };  

  return (
    <PageLayout>
      <div className="content-layout">
        <h1 id="page-title" className="content__title">
          User Profile
        </h1>
        <div className="content__body">
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
              <form onSubmit={handleSubmit}>
                <p id="page-description">
                  <span>
                    First Name
                  </span>
                  <span>
                    <input
                      type="text"
                      value={firstName}
                      placeholder="First Name"
                      onChange={(e) => setFirstName(e.target.value)}
                    />
                  </span>
                </p>

                <p id="page-description">
                  <span>
                    Last Name
                  </span>
                  <span>
                    <input
                      type="text"
                      value={lastName}
                      placeholder="Last Name"
                      onChange={(e) => setLastName(e.target.value)}
                    />
                  </span>
                </p>

                <p id="page-description">
                  <span>
                    Phone
                  </span>
                  <span>
                    <input
                      type="text"
                      value={phoneNumber}
                      placeholder="000-000-0000"
                      onChange={(e) => setPhoneNumber(e.target.value)}
                    />
                  </span>
                </p>
                <p id="page-description">
                  <span>
                    Email
                  </span>
                  <span>
                    <input
                      type="text"
                      value={email}
                      placeholder="user@example.com"
                      onChange={(e) => setEmail(e.target.value)}
                    />
                  </span>
                </p>
                <p id="page-description">
                  <span>
                    Max Distance (miles)
                  </span>
                  <span>
                    <input
                      type="text"
                      value={maxDistance}
                      placeholder="1"
                      onChange={(e) => setMaxDistance(e.target.value)}
                    />
                  </span>
                </p>

                <p id="page-description">
                  <span>
                    Address
                  </span>
                  <span>
                    <input
                      type="text"
                      value={addressLine1}
                      placeholder="Line 1"
                      onChange={(e) => setAddressLine1(e.target.value)}
                    />
                  </span>
                  <span>
                    <input
                      type="text"
                      value={addressLine2}
                      placeholder="Line 2"
                      onChange={(e) => setAddressLine2(e.target.value)}
                    />
                  </span>
                  <span>
                    <input
                      type="text"
                      value={addressCity}
                      placeholder="City"
                      onChange={(e) => setAddressCity(e.target.value)}
                    />
                  </span>
                  <span>
                    <input
                      type="text"
                      value={addressState}
                      placeholder="State"
                      onChange={(e) => setAddressState(e.target.value)}
                    />
                  </span>
                  <span>
                    <input
                      type="text"
                      value={addressZipCode}
                      placeholder="Zip Code"
                      onChange={(e) => setAddressZipCode(e.target.value)}
                    />
                  </span>
                </p>

                <button type="submit">Update</button>

                <div className="message">{message ? <p>{message}</p> : null}</div>
              </form>
            </div>
          </div>
        </div>
      </div>
    </PageLayout>
  );
};
