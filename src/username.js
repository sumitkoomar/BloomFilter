import React, { useState, useEffect } from "react";

const UsernameForm = () => {
  const [username, setUsername] = useState("");
  const [available, setAvailable] = useState(null);
  const [message, setMessage] = useState("");
  const [messageType, setMessageType] = useState(""); // "success" | "error"
  const [typingTimeout, setTypingTimeout] = useState(null);

  useEffect(() => {
    if (!username.trim()) {
      setAvailable(null);
      setMessage("");
      return;
    }

    if (typingTimeout) clearTimeout(typingTimeout);

    const timeout = setTimeout(async () => {
      try {
        const res = await fetch(
          `https://bloomfilter-backend.onrender.com/create-username/check?username=${username}`
        );
        const data = await res.json();

        if (data.available) {
          setAvailable(true);
          setMessage(`✅ Username "${username}" is available.`);
          setMessageType("success");
        } else {
          setAvailable(false);
          setMessage(`❌ Username "${username}" is already taken.`);
          setMessageType("error");
        }
      } catch (err) {
        setAvailable(null);
        setMessage("⚠️ Failed to check availability.");
        setMessageType("error");
        console.error(err);
      }
    }, 400); // debounce delay

    setTypingTimeout(timeout);
  }, [username]);

  const addUsername = async () => {
    setMessage("");

    if (!username.trim()) {
      setMessage("Username cannot be empty.");
      setMessageType("error");
      return;
    }

    if (available === false) {
      setMessage("Username is already taken. Cannot add.");
      setMessageType("error");
      return;
    }

    try {
      const res = await fetch("https://bloomfilter-backend.onrender.com/create-username/add", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ username }),
      });

      if (res.ok) {
        setMessage(`✅ Username "${username}" has been successfully added.`);
        setMessageType("success");
        setAvailable(false); // prevent resubmitting the same
      } else {
        setMessage("Failed to add username.");
        setMessageType("error");
      }
    } catch (err) {
      setMessage("Error occurred while adding username.");
      setMessageType("error");
      console.error(err);
    }
  };

  return (
    <div style={styles.container}>
      <h2>Create Username</h2>
      <div style={styles.form}>
        <input
          type="text"
          value={username}
          placeholder="Enter username"
          onChange={(e) => setUsername(e.target.value)}
          style={styles.input}
        />
        <button onClick={addUsername} style={styles.button}>
          Add Username
        </button>
        {message && (
          <p style={messageType === "success" ? styles.success : styles.error}>
            {message}
          </p>
        )}
      </div>
    </div>
  );
};

const styles = {
  container: {
    maxWidth: "400px",
    margin: "50px auto",
    padding: "20px",
    border: "1px solid #ccc",
    borderRadius: "10px",
    textAlign: "center",
    fontFamily: "Arial, sans-serif",
  },
  form: {
    display: "flex",
    flexDirection: "column",
    gap: "10px",
  },
  input: {
    padding: "10px",
    fontSize: "16px",
  },
  button: {
    padding: "10px",
    backgroundColor: "#4CAF50",
    color: "white",
    border: "none",
    borderRadius: "5px",
    cursor: "pointer",
  },
  error: {
    color: "red",
  },
  success: {
    color: "green",
  },
};

export default UsernameForm;
