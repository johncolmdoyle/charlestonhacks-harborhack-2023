import { useAuth0 } from "@auth0/auth0-react";
import React from "react";
import { NavBarTab } from "./nav-bar-tab";

export const NavBarTabs = () => {
  const { isAuthenticated } = useAuth0();

  return (
    <div className="nav-bar__tabs">
      <NavBarTab path="/profile" label="Profile" />
      <NavBarTab path="/createRequest" label="Create Request" />
      <NavBarTab path="/myRequests" label="My Requests" />
      <NavBarTab path="/nearbyRequests" label="Neighbor's Requests" />
    </div>
  );
};
