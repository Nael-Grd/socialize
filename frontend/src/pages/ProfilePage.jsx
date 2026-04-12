import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";

export default function ProfilePage() {

    const [profile, setProfile] = useState(null);
    const [error, setError] = useState(null);
    const { username } = useParams();
    const navigate = useNavigate();

    useEffect(() => {

        const fetchProfile = async () => {

            const token = localStorage.getItem("jwt_token");
                if (!token) {
                    navigate("/");
                    return;
                }
                try {
                    const response = await fetch(`http://localhost:8080/api/users/${username}`, {
                        method: "GET",
                        headers: {
                            "Content-Type": "application/json",
                            "Authorization": `Bearer ${token}` 
                        }
                    });
                    if (response.ok) {
                        const data = await response.json(); 
                        setProfile(data); 
                    }
                    else {
                         ("Utilisateur introuvable ou erreur serveur.");
                    }
                } catch (error) {
                    console.error("Impossible de joindre le serveur", error);
                }
            };
            fetchProfile(); 
        }, [navigate, username]);


    if (error) {
        return <div className="text-center mt-20 text-red-500 font-bold">{error}</div>;
    }
    
    if (!profile) {
        return <div className="text-center mt-20 text-gray-500 text-xl">Chargement du profil...</div>;
    }

    return (
        <div className="flex flex-col items-center mt-20">
            <div className="flex flex-col items-center gap-4 border p-8 rounded shadow-md w-96 bg-white">
                
                {/* La fausse photo de profil (un rond avec la première lettre du pseudo) */}
                <div className="w-24 h-24 bg-blue-100 rounded-full flex items-center justify-center text-4xl font-bold text-blue-500 uppercase">
                    {profile.username.charAt(0)}
                </div>

                {/* Le pseudo */}
                <h2 className="text-3xl font-bold">@{profile.username}</h2>

                {/* La zone des compteurs */}
                <div className="flex w-full justify-around mt-4 border-t pt-4">
                    
                    <div className="flex flex-col items-center cursor-pointer hover:text-blue-500 transition-colors">
                        <span className="font-bold text-2xl">{profile.followersCount}</span>
                        <span className="text-gray-500 text-sm">Abonnés</span>
                    </div>

                    <div className="flex flex-col items-center cursor-pointer hover:text-blue-500 transition-colors">
                        <span className="font-bold text-2xl">{profile.followingCount}</span>
                        <span className="text-gray-500 text-sm">Abonnements</span>
                    </div>

                </div>

                {/* Le bouton d'abonnement (que l'on rendra cliquable juste après) */}
                <button className="mt-4 w-full bg-blue-500 text-white font-bold p-2 rounded hover:bg-blue-600 transition-colors">
                    {profile.isFollowedByCurrentUser ? "Se désabonner" : "S'abonner"}
                </button>

            </div>
        </div>
    );
    
}

