import { useState } from 'react';
import { useNavigate, Link, useLocation } from 'react-router-dom';

export default function Navbar() {
    const navigate = useNavigate();
    const location = useLocation(); 

    const [isSearchOpen, setIsSearchOpen] = useState(false);
    const [searchQuery, setSearchQuery] = useState("");
    const [searchResults, setSearchResults] = useState([]);

    const myUsername = localStorage.getItem("my_username");

    const handleLogout = () => {
        localStorage.clear(); 
        navigate("/");
    };
    const handleSearch = async (e) => {
        const query = e.target.value;
        setSearchQuery(query);

        if (query.trim().length === 0) {
            setSearchResults([]);
            return;
        }

        try {
            const token = localStorage.getItem("jwt_token");
            const response = await fetch(`http://localhost:8080/api/users/search?query=${query}`, {
                headers: { "Authorization": `Bearer ${token}` }
            });
            if (response.ok) {
                const data = await response.json();
                setSearchResults(data);
            }
        } catch (error) {
            console.error("Erreur lors de la recherche", error);
        }
    };
    
    return (
        <>
            <nav className="fixed top-0 w-full bg-white shadow-md p-4 flex justify-between items-center px-10 z-40">
                <Link to="/feed" className="text-2xl font-bold text-blue-600">Socialize 🌐</Link>
                
                <div className="flex gap-6 items-center">
                    
                    <button 
                        onClick={() => setIsSearchOpen(true)}
                        className="flex items-center gap-2 bg-gray-100 hover:bg-gray-200 px-4 py-2 rounded-full transition-colors font-medium text-gray-700"
                    >
                        🔍 Rechercher
                    </button>

                    <Link to="/feed" className="hover:text-blue-500 font-medium">Fil d'actu</Link>
                    
                    {myUsername && myUsername !== "undefined" && (
                        <Link to={`/profile/${myUsername}`} className="hover:text-blue-500 font-medium">
                            Mon Profil
                        </Link>
                    )}

                    <button 
                        onClick={handleLogout}
                        className="bg-red-100 text-red-600 px-4 py-2 rounded-lg hover:bg-red-200 transition-colors font-semibold"
                    >
                        Déconnexion
                    </button>
                </div>
            </nav>

            {isSearchOpen && (
                <div className="fixed inset-0 bg-[rgba(0,0,0,0.4)] z-50 flex justify-center items-start pt-20">
                    
                    {/* Clic dans le vide = fermer la recherche */}
                    <div className="absolute inset-0" onClick={() => setIsSearchOpen(false)}></div>
                    
                    {/* La boîte de recherche */}
                    <div className="bg-white w-[500px] rounded-xl shadow-2xl p-4 z-10 flex flex-col">
                        
                        <div className="flex justify-between items-center mb-4">
                            <input 
                                type="text"
                                autoFocus
                                placeholder="Chercher un profil (ex: Naël...)"
                                className="w-full bg-gray-100 p-3 rounded-lg outline-none focus:ring-2 focus:ring-blue-500 transition-all"
                                value={searchQuery}
                                onChange={handleSearch}
                            />
                            <button onClick={() => setIsSearchOpen(false)} className="ml-4 text-gray-400 hover:text-red-500 font-bold text-xl">✕</button>
                        </div>

                        {/* Liste des résultats */}
                        <div className="max-h-80 overflow-y-auto flex flex-col gap-1">
                            {searchQuery.length > 0 && searchResults.length === 0 ? (
                                <p className="text-gray-500 text-center py-4 italic">Aucun utilisateur trouvé.</p>
                            ) : (
                                searchResults.map(user => (
                                    <div 
                                        key={user.id} 
                                        className="flex items-center gap-3 p-3 hover:bg-gray-100 rounded-lg cursor-pointer transition-colors"
                                        onClick={() => {
                                            setIsSearchOpen(false); 
                                            setSearchQuery(""); 
                                            setSearchResults([]);
                                            navigate(`/profile/${user.username}`); 
                                        }}
                                    >
                                        <div className="w-10 h-10 bg-blue-100 rounded-full flex items-center justify-center font-bold text-blue-500 text-lg uppercase">
                                            {user.username.charAt(0)}
                                        </div>
                                        <span className="font-semibold text-gray-800 text-lg">@{user.username}</span>
                                    </div>
                                ))
                            )}
                        </div>

                    </div>
                </div>
            )}
        </>
    );
}