import { useState } from "react";

export default function PostCard({ post, currentUser, onEdit, onDelete, onLike, onAddComment, onDeleteComment, commentInputValue, onCommentChange }) {
    
    const [showComments, setShowComments] = useState(false);
    const [isEditing, setIsEditing] = useState(false);
    const [editedContent, setEditedContent] = useState(post.content);

    const isAuthor = currentUser === post.authorUsername;

    const handleSaveEdit = () => {
        onEdit(post.id, editedContent);
        setIsEditing(false);
    };

    return (
        <div className="bg-white p-5 rounded-lg shadow-sm border border-gray-200 mb-4 w-full transition-all hover:shadow-md">
            
            {/* EN-TÊTE */}
            <div className="flex justify-between items-start mb-4">
                <div className="flex items-center gap-3">
                    <div className="w-10 h-10 bg-blue-100 rounded-full flex items-center justify-center font-bold text-blue-500 uppercase">
                        {/* Petite sécurité ici avec le "?" */}
                        {post.authorUsername ? post.authorUsername.charAt(0) : "?"}
                    </div>
                    <span className="font-bold text-gray-900">@{post.authorUsername}</span>
                </div>
                <div className="flex flex-col items-end gap-2">
                    <span className="text-xs text-gray-400">
                        {post.createdAt ? new Date(post.createdAt).toLocaleDateString() : "Date inconnue"}
                    </span>
                    {/* BOUTONS MODIFIER / SUPPRIMER */}
                    {isAuthor && !isEditing && (
                        <div className="flex gap-3 text-sm">
                            <button onClick={() => setIsEditing(true)} className="text-gray-400 hover:text-blue-500 transition-colors">✏️ Modifier</button>
                            <button onClick={() => onDelete(post.id)} className="text-gray-400 hover:text-red-500 transition-colors">🗑️ Supprimer</button>
                        </div>
                    )}
                </div>
            </div>

            {/* CONTENU */}
            {isEditing ? (
                <div className="mb-4">
                    <textarea 
                        className="w-full border border-blue-300 p-3 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 min-h-[100px]"
                        value={editedContent}
                        onChange={(e) => setEditedContent(e.target.value)}
                    />
                    <div className="flex justify-end gap-2 mt-2">
                        <button onClick={() => setIsEditing(false)} className="text-gray-600 hover:bg-gray-100 px-3 py-1 rounded transition-colors text-sm font-medium">Annuler</button>
                        <button onClick={handleSaveEdit} className="bg-blue-500 hover:bg-blue-600 text-white px-3 py-1 rounded transition-colors text-sm font-bold">Sauvegarder</button>
                    </div>
                </div>
            ) : (
                <p className="text-gray-800 leading-relaxed whitespace-pre-wrap mb-4">
                    {post.content}
                </p>
            )}

            {/* BARRE D'ACTIONS (likes, coms) */}
            <div className="flex justify-between items-center mt-4 border-t pt-3">
                <button 
                    onClick={() => onLike(post.id)} 
                    className="flex items-center gap-2 text-red-500 hover:bg-red-50 px-3 py-1 rounded-full font-medium transition-colors"
                >
                    ❤️ {post.likes || post.likeCount || 0}
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
                                <div key={comment.id} className="text-sm bg-white p-2 rounded border border-gray-200 flex justify-between items-start">                                   
                                    <div>
                                        <span className="font-bold text-blue-600 mr-2">{comment.authorUsername}</span>
                                        <span className="text-gray-700">{comment.content}</span>
                                    </div>
                                    {currentUser === comment.authorUsername && (
                                        <button 
                                            onClick={() => onDeleteComment(comment.id, post.id)} 
                                            className="text-gray-400 hover:text-red-500 transition-colors ml-2"
                                            title="Supprimer mon commentaire">
                                            🗑️
                                        </button>
                                    )}
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