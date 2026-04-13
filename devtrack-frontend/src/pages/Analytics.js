import React, { useState, useEffect } from 'react';
import Navbar from '../components/Navbar';
import { analyticsAPI } from '../services/api';

function Analytics() {
  const [analytics, setAnalytics] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    fetchAnalytics();
  }, []);

  const fetchAnalytics = async () => {
    try {
      const response = await analyticsAPI.get();
      setAnalytics(response.data);
    } catch (error) {
      console.error('Failed to fetch analytics:', error);
      setError('Failed to load analytics');
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return (
      <>
        <Navbar />
        <div className="container">
          <div className="loading">Loading analytics...</div>
        </div>
      </>
    );
  }

  if (error) {
    return (
      <>
        <Navbar />
        <div className="container">
          <div className="error">{error}</div>
        </div>
      </>
    );
  }

  return (
    <>
      <Navbar />
      <div className="container">
        <h2 style={{ marginBottom: '1.5rem' }}>Analytics Dashboard</h2>

        {/* Success Metrics */}
        <div className="card">
          <h3>Success Metrics</h3>
          <div className="stats-grid" style={{ marginTop: '1rem' }}>
            <div className="stat-card">
              <h3>Success Rate</h3>
              <div className="stat-value" style={{ color: '#10b981' }}>
                {analytics?.successRate?.toFixed(1) || 0}%
              </div>
              <p style={{ fontSize: '0.875rem', color: '#6b7280', marginTop: '0.5rem' }}>
                Tasks completed
              </p>
            </div>
            <div className="stat-card">
              <h3>Current Streak</h3>
              <div className="stat-value" style={{ color: '#f59e0b' }}>
                {analytics?.currentStreak || 0}
              </div>
              <p style={{ fontSize: '0.875rem', color: '#6b7280', marginTop: '0.5rem' }}>
                Days in a row
              </p>
            </div>
            <div className="stat-card">
              <h3>Longest Streak</h3>
              <div className="stat-value" style={{ color: '#2563eb' }}>
                {analytics?.longestStreak || 0}
              </div>
              <p style={{ fontSize: '0.875rem', color: '#6b7280', marginTop: '0.5rem' }}>
                Days total
              </p>
            </div>
          </div>
        </div>

        {/* Time Distribution */}
        <div className="card">
          <h3>Time Distribution</h3>
          
          {/* By Project */}
          {analytics?.timeByProject && Object.keys(analytics.timeByProject).length > 0 && (
            <div style={{ marginTop: '1.5rem' }}>
              <h4 style={{ fontSize: '1rem', color: '#374151', marginBottom: '0.75rem' }}>
                By Project
              </h4>
              <div style={{ display: 'grid', gap: '0.5rem' }}>
                {Object.entries(analytics.timeByProject)
                  .sort((a, b) => b[1] - a[1])
                  .map(([project, minutes]) => (
                    <div key={project} style={{ display: 'flex', alignItems: 'center', gap: '1rem' }}>
                      <div style={{ minWidth: '150px', color: '#4b5563', fontWeight: 500 }}>
                        {project}
                      </div>
                      <div style={{ flex: 1, background: '#e5e7eb', borderRadius: '0.25rem', height: '1.5rem', overflow: 'hidden' }}>
                        <div 
                          style={{ 
                            background: '#2563eb', 
                            height: '100%', 
                            width: `${(minutes / Math.max(...Object.values(analytics.timeByProject))) * 100}%`,
                            display: 'flex',
                            alignItems: 'center',
                            paddingLeft: '0.5rem',
                            color: 'white',
                            fontSize: '0.875rem',
                            fontWeight: 500
                          }}
                        >
                          {Math.floor(minutes / 60)}h {minutes % 60}m
                        </div>
                      </div>
                    </div>
                  ))}
              </div>
            </div>
          )}

          {/* By Category */}
          {analytics?.timeByCategory && Object.keys(analytics.timeByCategory).length > 0 && (
            <div style={{ marginTop: '1.5rem' }}>
              <h4 style={{ fontSize: '1rem', color: '#374151', marginBottom: '0.75rem' }}>
                By Category (Tasks Count)
              </h4>
              <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(200px, 1fr))', gap: '1rem' }}>
                {Object.entries(analytics.timeByCategory).map(([category, count]) => (
                  <div 
                    key={category} 
                    style={{ 
                      background: '#f9fafb', 
                      padding: '1rem', 
                      borderRadius: '0.375rem',
                      border: '1px solid #e5e7eb'
                    }}
                  >
                    <div style={{ fontSize: '0.875rem', color: '#6b7280' }}>{category}</div>
                    <div style={{ fontSize: '1.5rem', fontWeight: 700, color: '#1f2937', marginTop: '0.25rem' }}>
                      {count}
                    </div>
                  </div>
                ))}
              </div>
            </div>
          )}

          {/* By Work Type */}
          {analytics?.timeByWorkType && Object.keys(analytics.timeByWorkType).length > 0 && (
            <div style={{ marginTop: '1.5rem' }}>
              <h4 style={{ fontSize: '1rem', color: '#374151', marginBottom: '0.75rem' }}>
                By Work Type
              </h4>
              <div style={{ display: 'grid', gap: '0.5rem' }}>
                {Object.entries(analytics.timeByWorkType)
                  .sort((a, b) => b[1] - a[1])
                  .map(([workType, minutes]) => (
                    <div key={workType} style={{ display: 'flex', alignItems: 'center', gap: '1rem' }}>
                      <div style={{ minWidth: '120px', color: '#4b5563', fontWeight: 500 }}>
                        {workType}
                      </div>
                      <div style={{ flex: 1, background: '#e5e7eb', borderRadius: '0.25rem', height: '1.5rem', overflow: 'hidden' }}>
                        <div 
                          style={{ 
                            background: '#10b981', 
                            height: '100%', 
                            width: `${(minutes / Math.max(...Object.values(analytics.timeByWorkType))) * 100}%`,
                            display: 'flex',
                            alignItems: 'center',
                            paddingLeft: '0.5rem',
                            color: 'white',
                            fontSize: '0.875rem',
                            fontWeight: 500
                          }}
                        >
                          {Math.floor(minutes / 60)}h {minutes % 60}m
                        </div>
                      </div>
                    </div>
                  ))}
              </div>
            </div>
          )}
        </div>

        {/* Focus Metrics */}
        <div className="card">
          <h3>Focus Areas</h3>
          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1.5rem', marginTop: '1rem' }}>
            <div>
              <h4 style={{ fontSize: '0.875rem', color: '#6b7280', marginBottom: '0.5rem' }}>
                Top Project
              </h4>
              <div style={{ 
                background: '#eff6ff', 
                padding: '1rem', 
                borderRadius: '0.375rem',
                fontSize: '1.25rem',
                fontWeight: 600,
                color: '#1e40af'
              }}>
                {analytics?.topProject || 'N/A'}
              </div>
            </div>
            <div>
              <h4 style={{ fontSize: '0.875rem', color: '#6b7280', marginBottom: '0.5rem' }}>
                Top Category
              </h4>
              <div style={{ 
                background: '#ecfdf5', 
                padding: '1rem', 
                borderRadius: '0.375rem',
                fontSize: '1.25rem',
                fontWeight: 600,
                color: '#065f46'
              }}>
                {analytics?.topCategory || 'N/A'}
              </div>
            </div>
          </div>
        </div>

        {/* Estimation Accuracy */}
        {(analytics?.estimationAccuracy !== null && analytics?.estimationAccuracy !== undefined) && (
          <div className="card">
            <h3>Estimation Accuracy</h3>
            <div className="stats-grid" style={{ marginTop: '1rem' }}>
              <div className="stat-card">
                <h3>Accuracy</h3>
                <div className="stat-value" style={{ color: '#8b5cf6' }}>
                  {analytics.estimationAccuracy.toFixed(1)}%
                </div>
                <p style={{ fontSize: '0.875rem', color: '#6b7280', marginTop: '0.5rem' }}>
                  How close are your estimates
                </p>
              </div>
              <div className="stat-card">
                <h3>Average Error</h3>
                <div className="stat-value" style={{ color: '#ef4444' }}>
                  {analytics.averageEstimationError || 0}
                </div>
                <p style={{ fontSize: '0.875rem', color: '#6b7280', marginTop: '0.5rem' }}>
                  Minutes off on average
                </p>
              </div>
            </div>
          </div>
        )}

        {/* No Data Message */}
        {(!analytics?.timeByProject || Object.keys(analytics.timeByProject).length === 0) &&
         (!analytics?.timeByCategory || Object.keys(analytics.timeByCategory).length === 0) &&
         (!analytics?.timeByWorkType || Object.keys(analytics.timeByWorkType).length === 0) && (
          <div className="card" style={{ textAlign: 'center', padding: '3rem' }}>
            <div style={{ fontSize: '3rem', marginBottom: '1rem' }}>📊</div>
            <h3 style={{ color: '#6b7280', marginBottom: '0.5rem' }}>
              Not enough data yet
            </h3>
            <p style={{ color: '#9ca3af' }}>
              Create some tasks and log coding sessions to see analytics
            </p>
          </div>
        )}
      </div>
    </>
  );
}

export default Analytics;
