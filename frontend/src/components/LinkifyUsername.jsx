import { Link } from 'react-router-dom';

const LinkifyUsername = ({ text }) => {
    const parts = text.split(/(@\w+)/g);  // Regex pour trouver @suivi de lettres/chiffres

    return (
        <span>
            {parts.map((part, index) => {
                if (part.startsWith('@')) {
                    const username = part.substring(1); // Enleve le @
                    return (
                        <Link 
                            key={index} 
                            to={`/profile/${username}`} 
                            className="text-blue-600 font-semibold hover:underline"
                        >
                            {part}
                        </Link>
                    );
                }
                return part;
            })}
        </span>
    );
};

export default LinkifyUsername;