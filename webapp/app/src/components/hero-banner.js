import React from "react";

export const HeroBanner = () => {
  const logo = "https://cdn.auth0.com/blog/developer-hub/react-logo.svg";

  return (
    <div className="hero-banner hero-banner--pink-yellow">
      <h1 className="hero-banner__headline">Hello World!</h1>
      <p className="hero-banner__description">
        Base app is up and running with authentication to the API backend!
      </p>
      <a
        id="code-sample-link"
        target="_blank"
        rel="noopener noreferrer"
        href="https://twitter.com/CTNeighbors"
        className="button button--secondary"
      >
        Check out the journey!
      </a>
    </div>
  );
};
