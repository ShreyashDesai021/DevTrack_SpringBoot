import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import Navbar from '../components/Navbar';
import { taskAPI, sessionAPI } from '../services/api';

function Dashboard() {
  const [stats, setStats] = useState({
    totalTasks: 0,
    completedTasks: 0,
    pendingTasks: 0,
    totalSessions: 0,
    totalHours: 0
  });
  const [recentTasks, setRecentTasks] = useState([]);
  const [recentSessions, setRecentSessions] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchDashboardData();
  }, []);

  const fetchDashboardData = async () => {
    try {
      const [tasksRes, sessionsRes] = await Promise.all([
        taskAPI.getAll(),
        sessionAPI.getAll()
      ]);

      const tasks = tasksRes.data;
      const sessions = sessionsRes.data;

      // Calculate stats
      const completedCount = tasks.filter(t => t.status === 'DONE').length;
      const pendingCount = tasks.filter(t => t.status === 'PENDING').length;
      const totalMinutes = sessions.reduce((sum, s) => sum + (s.durationMinutes || 0), 0);

      setStats({
        totalTasks: tasks.length,
        completedTasks: completedCount,
        pendingTasks: pendingCount,
        totalSessions: sessions.length,
        totalHours: (totalMinutes / 60).toFixed(1)
      });

      // Get recent items (last 5)
      setRecentTasks(tasks.slice(0, 5));
      setRecentSessions(sessions.slice(0, 5));
    } catch (error) {
      console.error('Failed to fetch dashboard data:', error);
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return (
      <>
        <Navbar />
        <div className="container">
          <div className="loading">Loading dashboard...</div>
        </div>
      </>
    );
  }

  return (
    <>
      <Navbar />
      <div className="container">
        <h2 style={{ marginBottom: '1.5rem' }}>Dashboard</h2>

        {/* Stats Grid */}
        <div className="stats-grid">
          <div className="stat-card">
            <h3>Total Tasks</h3>
            <div className="stat-value">{stats.totalTasks}</div>
          </div>
          <div className="stat-card">
            <h3>Completed</h3>
            <div className="stat-value" style={{ color: '#10b981' }}>
              {stats.completedTasks}
            </div>
          </div>
          <div className="stat-card">
            <h3>Pending</h3>
            <div className="stat-value" style={{ color: '#f59e0b' }}>
              {stats.pendingTasks}
            </div>
          </div>
          <div className="stat-card">
            <h3>Coding Hours</h3>
            <div className="stat-value" style={{ color: '#2563eb' }}>
              {stats.totalHours}h
            </div>
          </div>
        </div>

        {/* Recent Tasks */}
        <div className="card">
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1rem' }}>
            <h3>Recent Tasks</h3>
            <Link to="/tasks" style={{ color: '#2563eb', textDecoration: 'none' }}>
              View All →
            </Link>
          </div>
          {recentTasks.length === 0 ? (
            <p style={{ color: '#6b7280' }}>No tasks yet. Create your first task!</p>
          ) : (
            <table className="table">
              <thead>
                <tr>
                  <th>Title</th>
                  <th>Status</th>
                  <th>Priority</th>
                  <th>Category</th>
                </tr>
              </thead>
              <tbody>
                {recentTasks.map((task) => (
                  <tr key={task.id}>
                    <td>{task.title}</td>
                    <td>
                      <span className={`badge badge-${task.status.toLowerCase().replace('_', '-')}`}>
                        {task.status}
                      </span>
                    </td>
                    <td>
                      <span className={`badge badge-${task.priority?.toLowerCase() || 'medium'}`}>
                        {task.priority || 'MEDIUM'}
                      </span>
                    </td>
                    <td>{task.category || '-'}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>

        {/* Recent Sessions */}
        <div className="card">
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1rem' }}>
            <h3>Recent Sessions</h3>
            <Link to="/sessions" style={{ color: '#2563eb', textDecoration: 'none' }}>
              View All →
            </Link>
          </div>
          {recentSessions.length === 0 ? (
            <p style={{ color: '#6b7280' }}>No sessions yet. Log your first coding session!</p>
          ) : (
            <table className="table">
              <thead>
                <tr>
                  <th>Project</th>
                  <th>Duration</th>
                  <th>Work Type</th>
                  <th>Outcome</th>
                </tr>
              </thead>
              <tbody>
                {recentSessions.map((session) => (
                  <tr key={session.id}>
                    <td>{session.projectName}</td>
                    <td>{session.durationMinutes} min</td>
                    <td>{session.workType || '-'}</td>
                    <td>{session.outcome || '-'}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>
      </div>
    </>
  );
}

export default Dashboard;
