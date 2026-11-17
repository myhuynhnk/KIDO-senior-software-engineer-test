import React, { useState, useEffect } from "react";
import { request } from "./api/client";
export default function App() {
    const [token, setToken] = useState(null),
        [profile, setProfile] = useState(null),
        [page, setPage] = useState("profile");
    const [username, setUsername] = useState("user"),
        [password, setPassword] = useState("");
    const [currentPassword, setCurrentPassword] = useState(""),
        [newPassword, setNewPassword] = useState(""),
        [confirmPassword, setConfirmPassword] = useState("");
    const [users, setUsers] = useState([]),
        [message, setMessage] = useState(""),
        [error, setError] = useState(""),
        [loading, setLoading] = useState(false);
    const logout = () => {
        setToken(null);
        setProfile(null);
        setUsers([]);
        setPassword("");
        setPage("profile");
        setMessage("");
        setError("");
    };
    async function login(e) {
        e.preventDefault();
        setError("");
        setMessage("");
        setLoading(true);
        try {
            const result = await request("/api/auth/login", {
                method: "POST",
                body: { username, password },
            });
            const p = await request("/api/users/me", { token: result.accessToken });
            setToken(result.accessToken);
            setProfile(p);
            setPassword("");
            setPage("profile");
        } catch (e) {
            setError(e.message);
        } finally {
            setLoading(false);
        }
    }
    async function changePassword(e) {
        e.preventDefault();
        setError("");
        setMessage("");
        setLoading(true);
        try {
            await request("/api/users/me/password", {
                token,
                method: "PUT",
                body: { currentPassword, newPassword, confirmPassword },
            });
            logout();
            setMessage("Password changed. Sign in with your new password.");
            setCurrentPassword("");
            setNewPassword("");
            setConfirmPassword("");
        } catch (e) {
            setError(e.message);
        } finally {
            setLoading(false);
        }
    }
    async function showUsers() {
        setError("");
        setMessage("");
        setLoading(true);
        try {
            setUsers(await request("/api/admin/users", { token }));
            setPage("admin");
        } catch (e) {
            setError(e.message);
        } finally {
            setLoading(false);
        }
    }
    const isAdmin = profile?.role === "ROLE_ADMIN";
    return (
        <div className="container py-4" style={{ maxWidth: 800 }}>
            <header className="d-flex justify-content-between align-items-center mb-4">
                <h3>KIDO Auth Demo</h3>
                {token && (
                    <button className="btn btn-outline-secondary btn-sm" onClick={logout}>
                        Logout
                    </button>
                )}
            </header>
            {error && (
                <div className="alert alert-danger" role="alert">
                    {error}
                </div>
            )}
            {message && <div className="alert alert-success">{message}</div>}
            {!token ? (
                <form
                    onSubmit={login}
                    className="card p-4 mx-auto"
                    style={{ maxWidth: 420 }}
                >
                    <h4>Login</h4>
                    <label className="form-label mt-3">Username</label>
                    <input
                        className="form-control"
                        required
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                    />
                    <label className="form-label mt-3">Password</label>
                    <input
                        className="form-control"
                        type="password"
                        required
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                    />
                    <button disabled={loading} className="btn btn-primary mt-4">
                        {loading ? "Please wait..." : "Sign in"}
                    </button>
                </form>
            ) : (
                <>
                    <nav className="d-flex gap-2 mb-4">
                        <button
                            className="btn btn-outline-primary"
                            onClick={() => {
                                setPage("profile");
                                setError("");
                            }}
                        >
                            Profile
                        </button>
                        <button
                            className="btn btn-outline-primary"
                            onClick={() => {
                                setPage("password");
                                setError("");
                            }}
                        >
                            Change password
                        </button>
                        {isAdmin && (
                            <button className="btn btn-outline-primary" onClick={showUsers}>
                                Admin users
                            </button>
                        )}
                    </nav>
                    {page === "profile" && (
                        <div className="card p-4">
                            <h4>My Profile</h4>
                            <p>
                                <b>Username:</b> {profile.username}
                            </p>
                            <p>
                                <b>Full name:</b> {profile.fullName}
                            </p>
                            <p>
                                <b>Email:</b> {profile.email}
                            </p>
                            <p>
                                <b>Role:</b> {profile.role}
                            </p>
                        </div>
                    )}
                    {page === "password" && (
                        <form onSubmit={changePassword} className="card p-4">
                            <h4>Change Password</h4>
                            <label className="form-label mt-3">Current password</label>
                            <input
                                className="form-control"
                                type="password"
                                required
                                value={currentPassword}
                                onChange={(e) => setCurrentPassword(e.target.value)}
                            />
                            <label className="form-label mt-3">
                                New password (8+ characters)
                            </label>
                            <input
                                className="form-control"
                                type="password"
                                required
                                minLength={8}
                                value={newPassword}
                                onChange={(e) => setNewPassword(e.target.value)}
                            />
                            <label className="form-label mt-3">Confirm new password</label>
                            <input
                                className="form-control"
                                type="password"
                                required
                                value={confirmPassword}
                                onChange={(e) => setConfirmPassword(e.target.value)}
                            />
                            <button disabled={loading} className="btn btn-primary mt-4">
                                Save new password
                            </button>
                        </form>
                    )}
                    {page === "admin" && (
                        <div className="card p-4">
                            <h4>Users (Admin only)</h4>
                            <div className="table-responsive">
                                <table className="table">
                                    <thead>
                                        <tr>
                                            <th>Username</th>
                                            <th>Name</th>
                                            <th>Role</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        {users.map((u) => (
                                            <tr key={u.id}>
                                                <td>{u.username}</td>
                                                <td>{u.fullName}</td>
                                                <td>{u.role}</td>
                                            </tr>
                                        ))}
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    )}
                </>
            )}
            <p className="text-muted small mt-4">
                Assessment demo · Token kept in memory (refresh requires sign-in)
            </p>
        </div>
    );
}
