package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.*;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = "*")
@SpringBootApplication
public class EdgeRankServer {

	public static void main(String[] args) {
		SpringApplication.run(EdgeRankServer.class, args);
	}
}

@RestController
@RequestMapping("/api")
class EdgeRankController {

	// Constants for EdgeRank
	private static final double POIDS_REACTION = 0.1;
	private static final double POIDS_COMMENTAIRE = 7.0;
	private static final double POIDS_PARTAGE = 3.0;
	private static final double FACTEUR_DECROISSANCE_TEMPS = 0.8;

	// Sample data
	private final List<Publication> publications = Arrays.asList(
			new Publication(1, "Hamza", "Bonjour tout le monde !", Instant.now().getEpochSecond() - 3600, 10, 4, 2),
			new Publication(2, "Gaby", "Le café ce matin était parfait ☕", Instant.now().getEpochSecond() - 7200, 8, 2, 1),
			new Publication(3, "Michel", "Regardez ce coucher de soleil ! 🌅", Instant.now().getEpochSecond() - 86400, 15, 5, 3),
			new Publication(4, "Hamza", "Quelqu'un veut jouer au foot ce weekend ?", Instant.now().getEpochSecond() - 172800, 5, 1, 0),
			new Publication(5, "Gaby", "Apprendre Java est vraiment amusant !", Instant.now().getEpochSecond() - 259200, 12, 3, 4)
	);

	private final Map<String, Map<String, Double>> affinites = new HashMap<>() {{
		put("Hamza", Map.of("Gaby", 0.8, "Michel", 0.5));
		put("Gaby", Map.of("Hamza", 0.7, "Michel", 0.6));
		put("Michel", Map.of("Hamza", 0.6, "Gaby", 0.4));
	}};

	@GetMapping("/feed")
	public List<PublicationScore> getFeed(@RequestParam String utilisateur) {
		return genererFil(utilisateur, publications, affinites);
	}

	private List<PublicationScore> genererFil(String utilisateur, List<Publication> publications, Map<String, Map<String, Double>> affinites) {
		List<PublicationScore> fil = new ArrayList<>();
		for (Publication publication : publications) {
			double affinite = affinites.getOrDefault(utilisateur, new HashMap<>()).getOrDefault(publication.auteur, 0.1);
			double score = calculerScoreEdgeRank(publication, affinite);
			fil.add(new PublicationScore(publication, score));
		}
		fil.sort((a, b) -> Double.compare(b.score, a.score)); // Trier par score décroissant
		return fil;
	}

	private double calculerScoreEdgeRank(Publication publication, double affiniteUtilisateur) {
		double scoreDeBase = (POIDS_REACTION * publication.reactions)
				+ (POIDS_COMMENTAIRE * publication.commentaires)
				+ (POIDS_PARTAGE * publication.partages);
		double facteurTemps = facteurDecroissanceTemps(publication.timestamp);
		return affiniteUtilisateur * scoreDeBase * facteurTemps;
	}

	private double facteurDecroissanceTemps(long timestamp) {
		long maintenant = Instant.now().getEpochSecond();
		long deltaTemps = maintenant - timestamp;
		double joursEcoules = deltaTemps / 86400.0;
		return Math.pow(FACTEUR_DECROISSANCE_TEMPS, joursEcoules);
	}

	static class Publication {
		int id;
		String auteur;
		String message;
		long timestamp;
		int reactions;
		int commentaires;
		int partages;

		public Publication(int id, String auteur, String message, long timestamp, int reactions, int commentaires, int partages) {
			this.id = id;
			this.auteur = auteur;
			this.message = message;
			this.timestamp = timestamp;
			this.reactions = reactions;
			this.commentaires = commentaires;
			this.partages = partages;
		}

		// Getters for serialization
		public int getId() { return id; }
		public String getAuteur() { return auteur; }
		public String getMessage() { return message; }
		public long getTimestamp() { return timestamp; }
		public int getReactions() { return reactions; }
		public int getCommentaires() { return commentaires; }
		public int getPartages() { return partages; }
	}

	static class PublicationScore {
		Publication publication;
		double score;

		public PublicationScore(Publication publication, double score) {
			this.publication = publication;
			this.score = score;
		}

		// Getters for serialization
		public Publication getPublication() { return publication; }
		public double getScore() { return score; }
	}
}
