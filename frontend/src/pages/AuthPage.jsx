import { useState } from 'react';
import { useNavigate } from 'react-router-dom';

export default function AuthPage() {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [username, setUsername] = useState("");
    const [isLoginView, setIsLoginView] = useState(true);
    // C'est cette ligne qui manquait !
    const [isLoading, setIsLoading] = useState(false); 
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (isLoading) return; // Empêche la boucle
        setIsLoading(true);

        const endpoint = isLoginView ? "/api/auth/login" : "/api/auth/register";
        const bodyData = isLoginView 
            ? { email, password } 
            : { username, email, password };

        try {
            const response = await fetch(`${import.meta.env.VITE_API_URL}${endpoint}`, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(bodyData),
            });

            if (response.ok) {
                const data = await response.json();
                localStorage.setItem("jwt_token", data.token);
                localStorage.setItem("my_username", data.username);
                alert("Succès !");
                navigate("/feed");
            } else {
                alert("Erreur lors de la requête.");
            }
        } catch (error) {
            console.error(error);
            alert("Impossible de joindre le serveur.");
        } finally {
            setIsLoading(false);
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
                <button 
                    type="submit" 
                    disabled={isLoading} 
                    className={`p-2 rounded ${isLoading ? "bg-gray-400" : "bg-blue-500"} text-white`}
                >
                    {isLoading ? "Chargement..." : (isLoginView ? "Se connecter" : "S'inscrire")}
                </button>
            </form>
            <p className="mt-4 cursor-pointer text-blue-500 underline" onClick={() => setIsLoginView(!isLoginView)}>
                {isLoginView ? "Pas de compte ? S'inscrire" : "Déjà un compte ? Se connecter"}
            </p>
        </div>
    );
}