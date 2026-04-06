import { useState } from 'react';
import { useNavigate } from 'react-router-dom';

export default function LoginPage() {

	const [email, setEmail] = useState("");    // boites memoire
	const [password, setPassword] = useState("");
	const navigate = useNavigate();
	
	const handleLogin = async (e) => {    // s'active quand on clique sur le boutton
		e.preventDefault();
		console.log("Tentative de connexion avec :", email, password);
    	try {
      
      		const response = await fetch("http://localhost:8080/api/auth/login", {    // On frappe à la porte de Spring Boot 
        		method: "POST", // On envoie des données
       			headers: {
          			"Content-Type": "application/json", // On prévient qu'on parle en JSON
        		},
        		body: JSON.stringify({
          			email: email,
          			password: password,
        		}),
      	});

      if (response.ok) {             
      	const token = await response.text(); 
        console.log("Connexion réussie ! Voici le Token :", token);     
        localStorage.setItem("jwt_token", token);
        navigate("/feed");
        
        alert("Connecté avec succès ! Regarde la console F12.");
      } else {
        console.error("Échec de la connexion.");
        alert("Mauvais email ou mot de passe.");
      }
    } catch (error) {           // Si Spring Boot est éteint ou s'il y a un problème CORS
      console.error("Erreur réseau (Spring Boot est-il allumé ?) :", error);
      alert("Impossible de joindre le serveur.");
    }
	};
	
	return (
    <div className="flex flex-col items-center mt-20">
      <h2 className="text-2xl font-bold mb-4">Connexion</h2>
      
      <form onSubmit={handleLogin} className="flex flex-col gap-4 border p-6 rounded shadow-md">
        
        <input 
          type="email" 
          placeholder="Ton email"
          className="border p-2 rounded"
          value={email} 
          onChange={(e) => setEmail(e.target.value)} 
        />

        <input 
          type="password" 
          placeholder="Ton mot de passe"
          className="border p-2 rounded"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />

        <button type="submit" className="bg-blue-500 text-white p-2 rounded">
          Se connecter
        </button>
      </form>
    </div>
  );
	
	 
}