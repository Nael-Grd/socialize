import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import PostCard from "../components/PostCard";

export default function ProfilePage() {

    const [profile, setProfile] = useState(null);
    const [error, setError] = useState(null);
    const { username } = useParams();
    const [newComments, setNewComments] = useState({});

    const [isModalOpen, setIsModalOpen] = useState(false);
    const [modalTitle, setModalTitle] = useState("");
    const [modalUsers, setModalUsers] = useState([]);
    
    const [userPosts, setUserPosts] = useState([]);
    const [currentPage, setCurrentPage] = useState(0);
    const [totalPages, setTotalPages] = useState(1);
    const [isLoadingPosts, setIsLoadingPosts] = useState(false);

    const myUsername = localStorage.getItem("my_username");
    const navigate = useNavigate();

    useEffect(() => {
        const fetchProfile = async () => {
            const token = localStorage.getItem("jwt_token");
            if (!token) {
                navigate("/");
                return;
            }
            try {
                const response = await fetch(`${import.meta.env.VITE_API_URL}/api/users/${username}`, {
                    method: "GET",
                    headers: {
                        "Content-Type": "application/json",
                        "Authorization": `Bearer ${token}` 
                    }
                });
                if (response.status === 401 || response.status === 403) {
                    console.warn("Session expirée, déconnexion automatique.");
                    localStorage.clear(); 
                    navigate("/");        
                    return;               
                }
                if (response.ok) {
                    const data = await response.json(); 
                    setProfile(data); 
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

        const isCurrentlyFollowing = profile.isFollowedByCurrentUser;
        const endpoint = isCurrentlyFollowing ? "unfollow" : "follow";

        try {
            const response = await fetch(`${import.meta.env.VITE_API_URL}/api/users/${endpoint}/${profile.id}`, {
                method: "POST", 
                headers: { "Authorization": `Bearer ${token}` }
            });
            if (response.status === 401 || response.status === 403) {
                localStorage.clear();
                navigate("/");
                return;
            }
            if (response.ok) {
                setProfile(prevProfile => ({
                    ...prevProfile,
                    isFollowedByCurrentUser: !isCurrentlyFollowing,
                    followersCount: isCurrentlyFollowing 
                        ? prevProfile.followersCount - 1 
                        : prevProfile.followersCount + 1
                }));
            }
        } catch (error) {
            console.error("Impossible de joindre le serveur", error);
        }
    };

    const openFollowModal = async (type) => {
        setModalTitle(type === "followers" ? "Abonnés" : "Abonnements");
        setIsModalOpen(true);
        setModalUsers([]); 

        const token = localStorage.getItem("jwt_token");
        try {
            const response = await fetch(`${import.meta.env.VITE_API_URL}/api/users/${username}/${type}`, {
                headers: { "Authorization": `Bearer ${token}` }
            });
            if (response.status === 401 || response.status === 403) {
                localStorage.clear();
                navigate("/");
                return;
            }
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
            const response = await fetch(`${import.meta.env.VITE_API_URL}/api/posts/${username}/posts?page=${pageToLoad}&size=5`, {
                headers: { "Authorization": `Bearer ${token}` }
            });
            if (response.status === 401 || response.status === 403) {
                localStorage.clear();
                navigate("/");
                return;
            }
            if (response.ok) {
                const data = await response.json();
                if (pageToLoad === 0) {
                    setUserPosts(data.content);
                } else {
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
    };

    // CORRIGÉ : On utilise setUserPosts et userPosts au lieu de setPosts/posts
    const handleLike = async (postId) => {
        const token = localStorage.getItem("jwt_token");
        try {
            const response = await fetch(`${import.meta.env.VITE_API_URL}/api/likes/${postId}`, {
                method: "POST",
                headers: { "Authorization": `Bearer ${token}` }
            });
            if (response.status === 401 || response.status === 403) {
                localStorage.clear();
                navigate("/");
                return;
            }
            if (response.ok) {
                const isLiked = await response.json(); 
                setUserPosts(userPosts.map(post => 
                    post.id === postId 
                        ? { ...post, likeCount: isLiked ? post.likeCount + 1 : Math.max(0, post.likeCount - 1) } 
                        : post
                ));
            }
        } catch (error) {
            console.error("Erreur réseau lors du like", error);
        }
    };
    
    // CORRIGÉ : On utilise setUserPosts et userPosts
    const handleAddComment = async (e, postId) => {
        e.preventDefault();
        const commentContent = newComments[postId];
        if (!commentContent || !commentContent.trim()) return;

        const token = localStorage.getItem("jwt_token");
        try {
            const response = await fetch(`${import.meta.env.VITE_API_URL}/api/comments`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${token}`
                },
                body: JSON.stringify({ content: commentContent, postId: postId }) 
            });
            if (response.status === 401 || response.status === 403) {
                localStorage.clear();
                navigate("/");
                return;
            }
            if (response.ok) {
                const createdComment = await response.json();
                setUserPosts(userPosts.map(post => {
                    if (post.id === postId) {
                        return { ...post, comments: [...(post.comments || []), createdComment] };
                    }
                    return post;
                }));
                setNewComments({ ...newComments, [postId]: "" });
            }
        } catch (error) {
            console.error("Erreur réseau", error);
        }
    };

    const handleDeletePost = async (postId) => {
        if (!window.confirm("Voulez-vous vraiment supprimer ce post ?")) return;
        try {
            const token = localStorage.getItem("jwt_token");
            const res = await fetch(`${import.meta.env.VITE_API_URL}/api/posts/${postId}`, {
                method: "DELETE",
                headers: { "Authorization": `Bearer ${token}` }
            });
            if (res.ok) {
                setUserPosts(userPosts.filter(p => p.id !== postId)); 
            }
        } catch (error) {
            console.error("Erreur suppression", error);
        }
    };

    const handleEditPost = async (postId, newContent) => {
        try {
            const token = localStorage.getItem("jwt_token");
            const res = await fetch(`${import.meta.env.VITE_API_URL}/api/posts/${postId}`, {
                method: "PUT",
                headers: { 
                    "Authorization": `Bearer ${token}`,
                    "Content-Type": "text/plain"
                },
                body: newContent
            });
            if (res.ok) {
                const updatedPost = await res.json();
                setUserPosts(userPosts.map(p => p.id === postId ? updatedPost : p));
            }
        } catch (error) {
            console.error("Erreur modification", error);
        }
    };

    const handleDeleteComment = async (commentId, postId) => {
        if (!window.confirm("Voulez-vous vraiment supprimer ce commentaire ?")) return;
        
        try {
            const token = localStorage.getItem("jwt_token");
            const res = await fetch(`${import.meta.env.VITE_API_URL}/api/comments/${commentId}`, {
                method: "DELETE",
                headers: { "Authorization": `Bearer ${token}` }
            });

            if (res.status === 401 || res.status === 403) {
                localStorage.clear();
                navigate("/");
                return;
            }
            if (res.ok) {
                setUserPosts(userPosts.map(post => {
                    if (post.id === postId) {
                        return {
                            ...post,
                            comments: post.comments.filter(c => c.id !== commentId)
                        };
                    }
                    return post;
                }));
            }
        } catch (error) {
            console.error("Erreur suppression commentaire", error);
        }
    };
    
    if (!profile) {
        return <div className="text-center mt-20 text-gray-500 text-xl">Chargement du profil...</div>;
    }

    return (
        <div className="flex flex-col items-center min-h-screen bg-gray-50 pt-20">
            
           <div className="w-full max-w-[400px] px-4 mb-4 flex justify-start">
                <button 
                    onClick={() => navigate(-1)} 
                    className="text-blue-500 hover:text-blue-700 font-medium flex items-center gap-2 transition-colors text-sm"
                >
                    <span className="text-lg">⬅</span> Retour
                </button>
            </div>

            <div className="flex flex-col items-center bg-white border border-gray-300 shadow-sm rounded-md p-8 w-full max-w-[400px] mx-4">                
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

                {myUsername !== profile.username && (
                    <button 
                        onClick={handleFollowClick} 
                        className={`w-full font-bold py-2.5 rounded-md transition-colors text-sm mt-4 ${
                            profile.isFollowedByCurrentUser 
                                ? "bg-gray-200 text-black hover:bg-gray-300" 
                                : "bg-blue-500 text-white hover:bg-blue-600" 
                        }`}
                    >
                        {profile.isFollowedByCurrentUser ? "Se désabonner" : "S'abonner"}
                    </button>
                )}
            </div>

            {/* MODAL (Inchangé) */}
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

            {/* CORRIGÉ : SECTION DES PUBLICATIONS (Nettoyage des doublons et responsive) */}
            <div className="w-full max-w-[400px] px-4 mt-8 flex flex-col mb-20">
                <h3 className="font-bold text-xl text-gray-800 border-b-2 border-blue-500 pb-2 self-start mb-6">
                    Publications
                </h3>

                {userPosts.length === 0 && !isLoadingPosts ? (
                    <div className="bg-white p-8 rounded-md border border-gray-200 text-center text-gray-500 italic shadow-sm">
                        Aucune publication pour le moment.
                    </div>
                ) : (
                    <>
                        {userPosts.map((post) => (
                            <PostCard 
                                key={post.id} 
                                post={post} 
                                currentUser={myUsername}        
                                onEdit={handleEditPost}         
                                onDelete={handleDeletePost}
                                onDeleteComment={handleDeleteComment}
                                onLike={handleLike}
                                onAddComment={(e) => handleAddComment(e, post.id)}
                                commentInputValue={newComments[post.id] || ""}
                                onCommentChange={(e) => setNewComments({ ...newComments, [post.id]: e.target.value })}
                            />
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