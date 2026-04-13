import React, { useState, useEffect } from 'react';
import Navbar from '../components/Navbar';
import { sessionAPI } from '../services/api';

function Sessions() {
  const [sessions, setSessions] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showForm, setShowForm] = useState(false);
  const [formData, setFormData] = useState({
    projectName: '',
    summary: '',
    durationMinutes: '',
    sessionDate: new Date().toISOString().split('T')[0],
    workType: '',
    outcome: '',
    difficulty: '',
    tags: ''
  });
  const [error, setError] = useState('');

  useEffect(() => {
    fetchSessions();
  }, []);

  const fetchSessions = async () => {
    try {
      const response = await sessionAPI.getAll();
      setSessions(response.data);
    } catch (error) {
      console.error('Failed to fetch sessions:', error);
      setError('Failed to load sessions');
    } finally {
      setLoading(false);
    }
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');

    try {
      const sessionData = {
        ...formData,
        durationMinutes: parseInt(formData.durationMinutes)
      };

      await sessionAPI.create(sessionData);
      resetForm();
      fetchSessions();
    } catch (error) {
      setError(error.response?.data?.message || 'Failed to create session');
    }
  };

  const handleDelete = async (id) => {
    if (window.confirm('Are you sure you want to delete this session?')) {
      try {
        await sessionAPI.delete(id);
        fetchSessions();
      } catch (error) {
        setError('Failed to delete session');
      }
    }
  };

  const resetForm = () => {
    setFormData({
      projectName: '',
      summary: '',
      durationMinutes: '',
      sessionDate: new Date().toISOString().split('T')[0],
      workType: '',
      outcome: '',
      difficulty: '',
      tags: ''
    });
    setShowForm(false);
    setError('');
  };

  const formatDuration = (minutes) => {
    const hours = Math.floor(minutes / 60);
    const mins = minutes % 60;
    if (hours > 0) {
      return `${hours}h ${mins}m`;
    }
    return `${mins}m`;
  };

  if (loading) {
    return (
      <>
        <Navbar />
        <div className="container">
          <div className="loading">Loading sessions...</div>
        </div>
      </>
    );
  }

  return (
    <>
      <Navbar />
      <div className="container">
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1.5rem' }}>
          <h2>Coding Sessions</h2>
          <button 
            className="btn btn-primary" 
            onClick={() => setShowForm(!showForm)}
          >
            {showForm ? 'Cancel' : '+ Log Session'}
          </button>
        </div>

        {error && <div className="error">{error}</div>}

        {/* Session Form */}
        {showForm && (
          <div className="card">
            <h3>Log New Session</h3>
            <form onSubmit={handleSubmit}>
              <div className="form-group">
                <label>Project Name *</label>
                <input
                  type="text"
                  name="projectName"
                  value={formData.projectName}
                  onChange={handleInputChange}
                  required
                  placeholder="e.g., DevTrack Backend"
                />
              </div>

              <div className="form-group">
                <label>Summary</label>
                <textarea
                  name="summary"
                  value={formData.summary}
                  onChange={handleInputChange}
                  rows="3"
                  placeholder="What did you work on? (optional)"
                />
              </div>

              <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem' }}>
                <div className="form-group">
                  <label>Duration (minutes) *</label>
                  <input
                    type="number"
                    name="durationMinutes"
                    value={formData.durationMinutes}
                    onChange={handleInputChange}
                    required
                    min="1"
                    placeholder="e.g., 120"
                  />
                </div>

                <div className="form-group">
                  <label>Session Date *</label>
                  <input
                    type="date"
                    name="sessionDate"
                    value={formData.sessionDate}
                    onChange={handleInputChange}
                    required
                  />
                </div>
              </div>

              <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr 1fr', gap: '1rem' }}>
                <div className="form-group">
                  <label>Work Type</label>
                  <select name="workType" value={formData.workType} onChange={handleInputChange}>
                    <option value="">Select...</option>
                    <option value="Feature">Feature</option>
                    <option value="Bugfix">Bugfix</option>
                    <option value="Refactor">Refactor</option>
                    <option value="Learning">Learning</option>
                  </select>
                </div>

                <div className="form-group">
                  <label>Outcome</label>
                  <select name="outcome" value={formData.outcome} onChange={handleInputChange}>
                    <option value="">Select...</option>
                    <option value="Completed">Completed</option>
                    <option value="In Progress">In Progress</option>
                    <option value="Blocked">Blocked</option>
                    <option value="Prototype">Prototype</option>
                  </select>
                </div>

                <div className="form-group">
                  <label>Difficulty</label>
                  <select name="difficulty" value={formData.difficulty} onChange={handleInputChange}>
                    <option value="">Select...</option>
                    <option value="Easy">Easy</option>
                    <option value="Medium">Medium</option>
                    <option value="Hard">Hard</option>
                    <option value="Very Hard">Very Hard</option>
                  </select>
                </div>
              </div>

              <div className="form-group">
                <label>Tags</label>
                <input
                  type="text"
                  name="tags"
                  value={formData.tags}
                  onChange={handleInputChange}
                  placeholder="Comma-separated: React,Redux,API"
                />
              </div>

              <div style={{ display: 'flex', gap: '1rem' }}>
                <button type="submit" className="btn btn-primary">
                  Log Session
                </button>
                <button type="button" className="btn btn-secondary" onClick={resetForm}>
                  Cancel
                </button>
              </div>
            </form>
          </div>
        )}

        {/* Sessions List */}
        <div className="card">
          <h3>All Sessions ({sessions.length})</h3>
          {sessions.length === 0 ? (
            <p style={{ color: '#6b7280', textAlign: 'center', padding: '2rem' }}>
              No sessions logged yet. Start tracking your coding time!
            </p>
          ) : (
            <div>
              {sessions.map((session) => (
                <div 
                  key={session.id} 
                  style={{ 
                    border: '1px solid #e5e7eb', 
                    borderRadius: '0.375rem', 
                    padding: '1rem', 
                    marginBottom: '1rem' 
                  }}
                >
                  <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'start' }}>
                    <div style={{ flex: 1 }}>
                      <h4 style={{ marginBottom: '0.5rem', color: '#1f2937' }}>
                        {session.projectName}
                      </h4>
                      {session.summary && (
                        <p style={{ color: '#6b7280', marginBottom: '0.75rem' }}>
                          {session.summary}
                        </p>
                      )}
                      <div style={{ display: 'flex', gap: '1rem', flexWrap: 'wrap', fontSize: '0.875rem' }}>
                        <span style={{ color: '#2563eb', fontWeight: 500 }}>
                          ⏱️ {formatDuration(session.durationMinutes)}
                        </span>
                        <span style={{ color: '#6b7280' }}>
                          📅 {new Date(session.sessionDate).toLocaleDateString()}
                        </span>
                        {session.workType && (
                          <span className="badge" style={{ background: '#dbeafe', color: '#1e40af' }}>
                            {session.workType}
                          </span>
                        )}
                        {session.outcome && (
                          <span className="badge" style={{ background: '#d1fae5', color: '#065f46' }}>
                            {session.outcome}
                          </span>
                        )}
                        {session.difficulty && (
                          <span className="badge" style={{ background: '#fef3c7', color: '#92400e' }}>
                            {session.difficulty}
                          </span>
                        )}
                      </div>
                      {session.tags && (
                        <div style={{ marginTop: '0.5rem' }}>
                          {session.tags.split(',').map((tag, idx) => (
                            <span 
                              key={idx} 
                              style={{ 
                                display: 'inline-block',
                                background: '#f3f4f6', 
                                padding: '0.125rem 0.5rem', 
                                borderRadius: '0.25rem', 
                                fontSize: '0.75rem',
                                marginRight: '0.5rem',
                                color: '#4b5563'
                              }}
                            >
                              #{tag.trim()}
                            </span>
                          ))}
                        </div>
                      )}
                    </div>
                    <button 
                      className="btn btn-danger" 
                      onClick={() => handleDelete(session.id)}
                      style={{ padding: '0.375rem 0.75rem', marginLeft: '1rem' }}
                    >
                      Delete
                    </button>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>
      </div>
    </>
  );
}

export default Sessions;
