import { useNavigate, Link } from 'react-router-dom';

export default function Navbar() {

	const navigate = useNavigate();
	
	const handleLogout = () => {
		localStorage.removeItem("jwt_token")
		navigate("/")
	};
	
	return (
        <nav className="w-full bg-white shadow-md p-4 flex justify-between items-center px-10">
            <Link to="/feed" className="text-2xl font-bold text-blue-600">Socialize 🌐</Link>
            
            <div className="flex gap-6 items-center">
                <Link to="/feed" className="hover:text-blue-500 font-medium">Fil d'actu</Link>
                <Link to="/profile" className="hover:text-blue-500 font-medium">Mon Profil</Link>
                <button 
                    onClick={handleLogout}
                    className="bg-red-100 text-red-600 px-4 py-2 rounded-lg hover:bg-red-200 transition-colors font-semibold"
                >
                    Déconnexion
                </button>
            </div>
        </nav>
    );
}