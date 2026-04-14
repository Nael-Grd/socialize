import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import Navbar from '../components/Navbar';
import PostCard from "../components/PostCard";

export default function FeedPage() {
    const [posts, setPosts] = useState([]);
    const [newPostContent, setNewPostContent] = useState("");
    const [newComments, setNewComments] = useState({});
    const navigate = useNavigate();

    // Affichage du feed 
    useEffect(() => {
        const fetchPosts = async () => {
            const token = localStorage.getItem("jwt_token");
            if (!token) {
                navigate("/");
                return;
            }
            try {
                const response = await fetch("http://localhost:8080/api/posts", {
                    method: "GET",
                    headers: {
                        "Content-Type": "application/json",
                        "Authorization": `Bearer ${token}` 
                    }
                });
                if (response.ok) {
		            const data = await response.json(); 
		            const fetchedPosts = Array.isArray(data) ? data : data.content;
		            const sortedPosts = [...fetchedPosts].sort((a, b) => {
		                return new Date(b.createdAt) - new Date(a.createdAt);
		            });
		
		            setPosts(sortedPosts); 
		        }
            } catch (error) {
                console.error("Impossible de joindre le serveur", error);
            }
        };
        fetchPosts(); 
    }, [navigate]);
  
    // Créer un post
    const handleCreatePost = async (e) => {
        e.preventDefault();
        if (!newPostContent.trim()) return; 
        const token = localStorage.getItem("jwt_token");
        try {
            const response = await fetch("http://localhost:8080/api/posts", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${token}`
                },
                body: JSON.stringify({ content: newPostContent }) 
            });
            if (response.ok) {
                const createdPost = await response.json(); 
                setPosts([createdPost, ...posts]); 
                setNewPostContent(""); 
            }
        } catch (error) {
            console.error("Erreur réseau", error);
        }
    }; 
    
    // Liker un post
    const handleLike = async (postId) => {
        const token = localStorage.getItem("jwt_token");
        try {
            const response = await fetch(`http://localhost:8080/api/likes/${postId}`, {
                method: "POST",
                headers: { "Authorization": `Bearer ${token}` }
            });
            if (response.ok) {
                const isLiked = await response.json(); 
                setPosts(posts.map(post => 
                    post.id === postId 
                        ? { ...post, likeCount: isLiked ? post.likeCount + 1 : Math.max(0, post.likeCount - 1) } 
                        : post
                ));
            }
        } catch (error) {
            console.error("Erreur réseau lors du like", error);
        }
    };
    
    // Ajouter un commentaire
    const handleAddComment = async (e, postId) => {
        e.preventDefault();
        const commentContent = newComments[postId];
        if (!commentContent || !commentContent.trim()) return;

        const token = localStorage.getItem("jwt_token");
        try {
            const response = await fetch("http://localhost:8080/api/comments", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${token}`
                },
                body: JSON.stringify({ content: commentContent, postId: postId }) 
            });

            if (response.ok) {
                const createdComment = await response.json();
                setPosts(posts.map(post => {
                    if (post.id === postId) {
                        return {
                            ...post,
                            comments: [...(post.comments || []), createdComment]
                        };
                    }
                    return post;
                }));
                setNewComments({ ...newComments, [postId]: "" });
            }
        } catch (error) {
            console.error("Erreur réseau lors du commentaire", error);
        }
    };

    return (
        <>
            <Navbar />
            <div className="flex flex-col items-center mt-24 w-full bg-gray-50 min-h-screen">
                <h1 className="text-3xl font-bold text-blue-600 mb-8">Fil d'Actualité 📰</h1>
                
                <div className="w-full max-w-2xl flex flex-col gap-6 mb-10">
                    
                    {/* Zone de création de post */}
                    <form onSubmit={handleCreatePost} className="bg-white p-4 rounded shadow-md border flex flex-col gap-3">
                        <textarea 
                            className="w-full border p-3 rounded resize-none focus:outline-none focus:ring-2 focus:ring-blue-400"
                            rows="3"
                            placeholder="Quoi de neuf aujourd'hui ?"
                            value={newPostContent}
                            onChange={(e) => setNewPostContent(e.target.value)}
                        />
                        <button type="submit" className="bg-blue-500 hover:bg-blue-600 text-white font-bold py-2 px-4 rounded self-end transition-colors">
                            Publier
                        </button>
                    </form>

                    {/* LISTE DES POSTS */}
                    {posts.length === 0 ? (
                        <p className="text-gray-500 text-center mt-4">Aucun post pour le moment.</p>
                    ) : (
                        posts.map((post) => (
                            <PostCard 
                                key={post.id} 
                                post={post} 
                                onLike={handleLike}
                                onAddComment={(e) => handleAddComment(e, post.id)}
                                commentInputValue={newComments[post.id] || ""}
                                onCommentChange={(e) => setNewComments({ ...newComments, [post.id]: e.target.value })}
                            />
                        ))
                    )}
                </div>
            </div>
        </>
    );
}