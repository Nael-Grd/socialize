import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import PostCard from "../components/PostCard";

export default function ProfilePage() {

    const [profile, setProfile] = useState(null);
    const [error, setError] = useState(null);
    const { username } = useParams();

    const [isModalOpen, setIsModalOpen] = useState(false);
    const [modalTitle, setModalTitle] = useState("");
    const [modalUsers, setModalUsers] = useState([]);
    
    const [userPosts, setUserPosts] = useState([]);
    const [currentPage, setCurrentPage] = useState(0);
    const [totalPages, setTotalPages] = useState(1);
    const [isLoadingPosts, setIsLoadingPosts] = useState(false);

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

        const handleFollowClick = async () => {
            const token = localStorage.getItem("jwt_token");
            if (!token) return;

            // On regarde l'état actuel pour savoir si on doit s'abonner ou se désabonner
            const isCurrentlyFollowing = profile.isFollowedByCurrentUser;
            const endpoint = isCurrentlyFollowing ? "unfollow" : "follow";

            try {
                const response = await fetch(`http://localhost:8080/api/users/${endpoint}/${profile.id}`, {
                    method: "POST", 
                    headers: {
                        "Authorization": `Bearer ${token}`
                    }
                });

                if (response.ok) {
                    setProfile(prevProfile => ({
                        ...prevProfile, // On garde toutes les autres infos intactes
                        isFollowedByCurrentUser: !isCurrentlyFollowing, // On inverse le bouton
                        // On modifie le compteur d'abonnés en direct :
                        followersCount: isCurrentlyFollowing 
                            ? prevProfile.followersCount - 1 
                            : prevProfile.followersCount + 1
                    }));
                } else {
                    console.error("Erreur côté serveur lors de l'abonnement.");
                }
            } catch (error) {
                console.error("Impossible de joindre le serveur", error);
            }
        };

        const openFollowModal = async (type) => {
            setModalTitle(type === "followers" ? "Abonnés" : "Abonnements");
            setIsModalOpen(true);
            setModalUsers([]); // On vide l'ancienne liste pendant le chargement

            const token = localStorage.getItem("jwt_token");
            try {
                const response = await fetch(`http://localhost:8080/api/users/${username}/${type}`, {
                    headers: { "Authorization": `Bearer ${token}` }
                });
                if (response.ok) {
                    const data = await response.json();
                    setModalUsers(data);
                }
            } catch (error) {
                console.error("Erreur de chargement", error);
            }
        };

        const fetchUserPosts = async (pageToLoad) => {
            setIsLoadingPosts(true);
            const token = localStorage.getItem("jwt_token");
            
            try {
                const response = await fetch(`http://localhost:8080/api/posts/${username}/posts?page=${pageToLoad}&size=5`, {
                    headers: { "Authorization": `Bearer ${token}` }
                });
                
                if (response.ok) {
                    const data = await response.json();
                    
                    if (pageToLoad === 0) {
                        // Si c're la première page, on remplace tout
                        setUserPosts(data.content);
                    } else {
                        // Si c'est "Voir plus", on ajoute à la suite des posts existants
                        setUserPosts(prevPosts => [...prevPosts, ...data.content]);
                    }
                    setTotalPages(data.totalPages);
                }
            } catch (error) {
                console.error("Erreur de chargement des posts", error);
            } finally {
                setIsLoadingPosts(false);
            }
        };

        useEffect(() => {
            if (!username || username === "undefined") return;
        
            setCurrentPage(0);
            fetchUserPosts(0);
            
        }, [username]); 

        const loadMorePosts = () => {
            const nextPage = currentPage + 1;
            setCurrentPage(nextPage);
            fetchUserPosts(nextPage);
        }
    
    if (!profile) {
        return <div className="text-center mt-20 text-gray-500 text-xl">Chargement du profil...</div>;
    }

    return (
        <div className="flex flex-col items-center min-h-screen bg-gray-50 pt-20">
            
           <div className="w-[400px] mb-4 flex justify-start">
                <button 
                    onClick={() => navigate(-1)} 
                    className="text-blue-500 hover:text-blue-700 font-medium flex items-center gap-2 transition-colors text-sm"
                >
                    <span className="text-lg">⬅</span> Retour
                </button>
            </div>

            <div className="flex flex-col items-center bg-white border border-gray-300 shadow-sm rounded-md p-8 w-[400px]">
                
                <div className="w-24 h-24 bg-blue-100 rounded-full flex items-center justify-center text-4xl font-bold text-blue-500 uppercase mb-4">
                    {profile.username.charAt(0)}
                </div>

                <h2 className="text-2xl font-bold text-gray-900 mb-6">@{profile.username}</h2>

                <div className="flex w-full justify-around border-t border-gray-800 pt-6 mb-6">
                    
                    <div onClick={() => openFollowModal("followers")} className="flex flex-col items-center cursor-pointer hover:text-blue-500 transition-colors">
                        <span className="font-bold text-xl text-gray-900">
                            {profile.followersCount !== undefined ? profile.followersCount : 0}
                        </span>
                        <span className="text-gray-500 text-sm">Abonnés</span>
                    </div>

                    <div onClick={() => openFollowModal("following")} className="flex flex-col items-center cursor-pointer hover:text-blue-500 transition-colors">
                        <span className="font-bold text-xl text-gray-900">
                            {profile.followingCount !== undefined ? profile.followingCount : 0}
                        </span>
                        <span className="text-gray-500 text-sm">Abonnements</span>
                    </div>

                </div>

                <button 
                    onClick={handleFollowClick} 
                    className={`w-full font-bold py-2.5 rounded-md transition-colors text-sm ${
                        profile.isFollowedByCurrentUser 
                            ? "bg-gray-200 text-black hover:bg-gray-300" // Bouton GRIS quand on est abonné
                            : "bg-blue-500 text-white hover:bg-blue-600" // Bouton BLEU quand on n'est pas abonné
                    }`}
                >
                    {profile.isFollowedByCurrentUser ? "Se désabonner" : "S'abonner"}
                </button>

            </div>

            {isModalOpen && (
                <div className="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center z-50">
                    <div className="bg-white rounded-lg p-6 w-80 shadow-xl">
                        
                        <div className="flex justify-between items-center mb-4 border-b pb-2">
                            <h3 className="font-bold text-xl text-gray-800">{modalTitle}</h3>
                            <button onClick={() => setIsModalOpen(false)} className="text-gray-500 hover:text-red-500 font-bold transition-colors">✕</button>
                        </div>

                        <div className="max-h-60 overflow-y-auto flex flex-col gap-2">
                            {modalUsers.length === 0 ? (
                                <p className="text-gray-500 text-center italic py-4">Aucun utilisateur.</p>
                            ) : (
                                modalUsers.map((user) => (
                                    <div key={user.id} className="flex items-center gap-3 p-2 hover:bg-gray-100 rounded cursor-pointer transition-colors" 
                                         onClick={() => {
                                             setIsModalOpen(false);
                                             navigate(`/profile/${user.username}`); 
                                         }}>
                                        <div className="w-8 h-8 bg-blue-100 rounded-full flex items-center justify-center font-bold text-blue-500 text-sm uppercase">
                                            {user.username.charAt(0)}
                                        </div>
                                        <span className="font-medium text-gray-800">@{user.username}</span>
                                    </div>
                                ))
                            )}
                        </div>

                    </div>
                </div>
            )}

            {/* SECTION DES PUBLICATIONS SUR LE PROFIL */}
            <div className="w-[400px] mt-8 flex flex-col mb-20">
                <h3 className="font-bold text-xl text-gray-800 border-b-2 border-blue-500 pb-2 self-start mb-6">
                    Publications
                </h3>

                {userPosts.length === 0 && !isLoadingPosts ? (
                    <div className="bg-white p-8 rounded-md border border-gray-200 text-center text-gray-500 italic">
                        Aucune publication pour le moment.
                    </div>
                ) : (
                    <>
                        {/*  On appelle notre composant pour chaque post */}
                        {userPosts.map(post => (
                            <PostCard key={post.id} post={post} />
                        ))}

                        {/* Bouton Voir Plus */}
                        {currentPage < totalPages - 1 && (
                            <button 
                                onClick={loadMorePosts}
                                disabled={isLoadingPosts}
                                className="w-full py-3 mt-2 bg-gray-100 hover:bg-gray-200 text-gray-700 font-medium rounded-md transition-colors"
                            >
                                {isLoadingPosts ? "Chargement..." : "Voir plus de posts"}
                            </button>
                        )}
                    </>
                )}
            </div>

        </div>
        
    );

}

