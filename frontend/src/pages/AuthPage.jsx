import { useState } from 'react';
import { useNavigate } from 'react-router-dom';

export default function AuthPage() {

	const [email, setEmail] = useState("");    // boites memoire
	const [password, setPassword] = useState("");
  const [username, setUsername] = useState("");
  const [isLoginView, setIsLoginView] = useState(true);
	const navigate = useNavigate();
	
	const handleSubmit = async (e) => {
    e.preventDefault();
    
    if (isLoginView) {
        console.log("Tentative de connexion avec :", email, password);
        try {
            const response = await fetch("http://localhost:8080/api/auth/login", { 
                method: "POST",
                headers: {
                    "Content-Type": "application/json", 
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
                alert("Connecté avec succès !");
            } else {
                console.error("Échec de la connexion.");
                alert("Mauvais email ou mot de passe.");
            }
        } catch (error) {
            console.error("Erreur réseau :", error);
            alert("Impossible de joindre le serveur.");
        }
    } else {
        console.log("Tentative d'inscription avec :", username, email);
        try {
            const response = await fetch("http://localhost:8080/api/auth/register", {    
                method: "POST",
                headers: {
                    "Content-Type": "application/json", 
                },
                body: JSON.stringify({
                    username: username,
                    email: email,
                    password: password,
                }),
            });

            if (response.ok) {             
                const token = await response.text(); 
                console.log("Inscription réussie ! Voici le Token :", token);     
                localStorage.setItem("jwt_token", token);
                navigate("/feed");
                alert("Compte créé avec succès !");
            } else {
                console.error("Échec de l'inscription.");
                alert("Cet email est déjà utilisé ou les données sont invalides."); 
            }
        } catch (error) {
            console.error("Erreur réseau :", error);
            alert("Impossible de joindre le serveur.");
        }
    }
  };
         
     
	
	return (
    <div className="flex flex-col items-center mt-20">
      <h2 className="text-2xl font-bold mb-4">{isLoginView ? "Connexion" : "Créer un compte"}</h2>
      
      <form onSubmit={handleSubmit} className="flex flex-col gap-4 border p-6 rounded shadow-md">
        
        {!isLoginView && (  
        <input 
          type="text" 
          placeholder="Ton nom d'utilisateur"
          className="border p-2 rounded"
          value={username} 
          onChange={(e) => setUsername(e.target.value)} 
        />
        )}

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
          {isLoginView ? "Se connecter" : "S'inscrire"}
        </button>
      </form>
      <p className="mt-4 cursor-pointer text-blue-500 underline" onClick={() => setIsLoginView(!isLoginView)}>
        {isLoginView ? "Pas de compte ? S'inscrire" : "Déjà un compte ? Se connecter"}
      </p>
    </div>
  );
	
	 
}