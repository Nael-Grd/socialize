import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

export default function FeedPage() {
	
	const [posts, setPosts] = useState([]);
	const [newPostContent, setNewPostContent] = useState("");
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
          setPosts(data); 
        } else {
          console.error("Erreur, le token est peut-être expiré.");
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
        
        // Si la case est vide ou ne contient que des espaces, on annule
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
            } else {
                alert("Erreur lors de la publication.");
            }
        } catch (error) {
            console.error("Erreur réseau", error);
        }
    }; 

	return (
        <div className="flex flex-col items-center mt-10 w-full bg-gray-50 min-h-screen">
            <h1 className="text-3xl font-bold text-blue-600 mb-8">Ton Fil d'Actualité 📰</h1>
            
            <div className="w-full max-w-2xl flex flex-col gap-6">
                
                {/* Zone de création de post */}
                <form onSubmit={handleCreatePost} className="bg-white p-4 rounded shadow-md border flex flex-col gap-3">
                    <textarea 
                        className="w-full border p-3 rounded resize-none focus:outline-none focus:ring-2 focus:ring-blue-400"
                        rows="3"
                        placeholder="Quoi de neuf aujourd'hui ?"
                        value={newPostContent}
                        onChange={(e) => setNewPostContent(e.target.value)}
                    />
                    <button 
                        type="submit" 
                        className="bg-blue-500 hover:bg-blue-600 text-white font-bold py-2 px-4 rounded self-end transition-colors"
                    >
                        Publier
                    </button>
                </form>

                {/* Liste des posts */}
                {posts.length === 0 ? (
                    <p className="text-gray-500 text-center mt-4">Aucun post pour le moment. Sois le premier à écrire !</p>
                ) : (
                    posts.map((post) => (
                        <div key={post.id} className="border p-4 rounded shadow-sm bg-white">
                            <p className="font-semibold text-gray-800 mb-2">{post.author.username} a écrit :</p>
                            <p className="text-gray-600">{post.content}</p>
                            <span className="text-xs text-gray-400 mt-4 block">
                                Posté le : {new Date(post.createdAt).toLocaleDateString()}
                            </span>
                        </div>
                    ))
                )}
            </div>
        </div>
    );
}