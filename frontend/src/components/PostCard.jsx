import { useState } from "react";

export default function PostCard({ post, onLike, onAddComment, commentInputValue, onCommentChange }) {
    
    const [showComments, setShowComments] = useState(false);

    return (
        <div className="bg-white p-5 rounded-lg shadow-sm border border-gray-200 mb-4 w-full transition-all hover:shadow-md">
            
            {/* EN-TÊTE */}
            <div className="flex justify-between items-start mb-4">
                <div className="flex items-center gap-3">
                    <div className="w-10 h-10 bg-blue-100 rounded-full flex items-center justify-center font-bold text-blue-500 uppercase">
                        {post.authorUsername.charAt(0)}
                    </div>
                    <span className="font-bold text-gray-900">@{post.authorUsername}</span>
                </div>
                <span className="text-xs text-gray-400">
                    Posté le : {post.createdAt ? new Date(post.createdAt).toLocaleDateString() : "Date inconnue"}
                </span>
            </div>

            {/* CONTENU */}
            <p className="text-gray-800 leading-relaxed whitespace-pre-wrap mb-4">
                {post.content}
            </p>

            {/* BARRE D'ACTIONS (likes, coms) */}
            <div className="flex justify-between items-center mt-4 border-t pt-3">
                <button 
                    onClick={() => onLike(post.id)} 
                    className="flex items-center gap-2 text-red-500 hover:bg-red-50 px-3 py-1 rounded-full font-medium transition-colors"
                >
                    ❤️ {post.likeCount || 0}
                </button>
                
                {/* Bouton pour dérouler */}
                <button 
                    onClick={() => setShowComments(!showComments)}
                    className="flex items-center gap-1 text-gray-600 hover:text-blue-500 transition-colors font-medium text-sm px-3 py-1 rounded-full hover:bg-gray-50"
                >
                    💬 {post.comments ? post.comments.length : 0} Commentaires {showComments ? "▲" : "▼"}
                </button>
            </div>

            {/* ZONE DES COMMENTAIRES (Déroulable) */}
            {showComments && (
                <div className="mt-4 bg-gray-50 p-4 rounded-md border border-gray-100">
                    
                    {/* Liste des commentaires */}
                    <div className="flex flex-col gap-2 mb-3 max-h-40 overflow-y-auto">
                        {(!post.comments || post.comments.length === 0) ? (
                            <p className="text-xs text-gray-500 italic mb-2">Aucun commentaire.</p>
                        ) : (
                            post.comments.map((comment) => (
                                <div key={comment.id} className="text-sm bg-white p-2 rounded border border-gray-200">
                                    <span className="font-bold text-blue-600 mr-2">{comment.authorUsername}</span>
                                    <span className="text-gray-700">{comment.content}</span>
                                </div>
                            ))
                        )}
                    </div>

                    {/* Formulaire d'ajout de commentaire */}
                    <form onSubmit={onAddComment} className="flex gap-2 mt-2">
                        <input 
                            type="text" 
                            placeholder="Écrire un commentaire..." 
                            className="flex-1 border border-gray-300 rounded p-2 text-sm outline-none focus:border-blue-500"
                            value={commentInputValue}
                            onChange={onCommentChange}
                        />
                        <button type="submit" className="bg-gray-200 hover:bg-gray-300 text-sm font-semibold py-2 px-4 rounded transition-colors">
                            Envoyer
                        </button>
                    </form>
                </div>
            )}
        </div>
    );
}