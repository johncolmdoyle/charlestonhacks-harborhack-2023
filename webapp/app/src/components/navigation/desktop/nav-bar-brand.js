import React from "react";
import { NavLink } from "react-router-dom";

export const NavBarBrand = () => {
  return (
    <div className="nav-bar__brand">
      <NavLink to="/">
        <span>
          <p className="nav-bar__tab">ChuckTown Neighbors</p>
        </span>
      </NavLink>
    </div>
  );
};
