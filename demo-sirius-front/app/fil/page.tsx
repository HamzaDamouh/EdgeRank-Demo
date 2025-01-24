"use client";

import React, { useState } from "react";

const FeedPage: React.FC = () => {
  const [username, setUsername] = useState<string>(""); // Nom d'utilisateur
  const [feed, setFeed] = useState<any[]>([]); // Données du fil
  const [loading, setLoading] = useState<boolean>(false); // État de chargement
  const [error, setError] = useState<string | null>(null); // État d'erreur

  const fetchFeed = async () => {
    if (!username.trim()) {
      setError("Veuillez entrer un nom d'utilisateur valide.");
      return;
    }
    setError(null);
    setLoading(true);

    try {
      const response = await fetch(
        `http://localhost:8080/api/feed?utilisateur=${username}`
      );

      if (!response.ok) {
        throw new Error(`Erreur ${response.status}: ${response.statusText}`);
      }

      const data = await response.json();
      setFeed(data); // Met à jour le fil avec les données
    } catch (err: any) {
      setError("Impossible de récupérer le fil. Vérifiez le backend.");
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{ padding: "20px", fontFamily: "Arial, sans-serif" }}>
      <h1>EdgeRank Feed</h1>
      <div style={{ marginBottom: "20px" }}>
        <label htmlFor="username" style={{ marginRight: "10px" }}>
          Entrer un nom d'utilisateur :
        </label>
        <input
          type="text"
          id="username"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
          style={{
            padding: "5px",
            border: "1px solid #ccc",
            borderRadius: "4px",
            marginRight: "10px",
          }}
        />
        <button
          onClick={fetchFeed}
          style={{
            padding: "5px 10px",
            backgroundColor: "#0070f3",
            color: "white",
            border: "none",
            borderRadius: "4px",
            cursor: "pointer",
          }}
        >
          Charger le fil
        </button>
      </div>
      {loading && <p>Chargement du fil...</p>}
      {error && <p style={{ color: "red" }}>{error}</p>}
      <div>
        {feed.length > 0 ? (
          feed.map((item) => (
            <div
              key={item.publication.id}
              style={{
                border: "1px solid #ccc",
                padding: "10px",
                marginBottom: "10px",
                borderRadius: "5px",
              }}
            >
              <h3>{item.publication.message}</h3>
              <p>
                <strong>Auteur :</strong> {item.publication.auteur}
              </p>
              <p>
                <strong>Score :</strong> {item.score.toFixed(2)}
              </p>
              <p>
                <strong>Réactions :</strong> {item.publication.reactions},{" "}
                <strong>Commentaires :</strong>{" "}
                {item.publication.commentaires},{" "}
                <strong>Partages :</strong> {item.publication.partages}
              </p>
            </div>
          ))
        ) : (
          !loading && <p>Aucun post à afficher</p>
        )}
      </div>
    </div>
  );
};

export default FeedPage;
