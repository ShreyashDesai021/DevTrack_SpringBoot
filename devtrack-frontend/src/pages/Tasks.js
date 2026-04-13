import React, { useState, useEffect } from 'react';
import Navbar from '../components/Navbar';
import { taskAPI } from '../services/api';

function Tasks() {
  const [tasks, setTasks] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showForm, setShowForm] = useState(false);
  const [editingTask, setEditingTask] = useState(null);
  const [formData, setFormData] = useState({
    title: '',
    status: 'PENDING',
    priority: 'MEDIUM',
    category: '',
    estimatedMinutes: '',
    actualMinutes: ''
  });
  const [error, setError] = useState('');

  useEffect(() => {
    fetchTasks();
  }, []);

  const fetchTasks = async () => {
    try {
      const response = await taskAPI.getAll();
      setTasks(response.data);
    } catch (error) {
      console.error('Failed to fetch tasks:', error);
      setError('Failed to load tasks');
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
      const taskData = {
        ...formData,
        estimatedMinutes: formData.estimatedMinutes ? parseInt(formData.estimatedMinutes) : null,
        actualMinutes: formData.actualMinutes ? parseInt(formData.actualMinutes) : null
      };

      if (editingTask) {
        await taskAPI.update(editingTask.id, taskData);
      } else {
        await taskAPI.create(taskData);
      }

      resetForm();
      fetchTasks();
    } catch (error) {
      setError(error.response?.data?.message || 'Failed to save task');
    }
  };

  const handleEdit = (task) => {
    setEditingTask(task);
    setFormData({
      title: task.title,
      status: task.status,
      priority: task.priority || 'MEDIUM',
      category: task.category || '',
      estimatedMinutes: task.estimatedMinutes || '',
      actualMinutes: task.actualMinutes || ''
    });
    setShowForm(true);
  };

  const handleDelete = async (id) => {
    if (window.confirm('Are you sure you want to delete this task?')) {
      try {
        await taskAPI.delete(id);
        fetchTasks();
      } catch (error) {
        setError('Failed to delete task');
      }
    }
  };

  const resetForm = () => {
    setFormData({
      title: '',
      status: 'PENDING',
      priority: 'MEDIUM',
      category: '',
      estimatedMinutes: '',
      actualMinutes: ''
    });
    setEditingTask(null);
    setShowForm(false);
    setError('');
  };

  if (loading) {
    return (
      <>
        <Navbar />
        <div className="container">
          <div className="loading">Loading tasks...</div>
        </div>
      </>
    );
  }

  return (
    <>
      <Navbar />
      <div className="container">
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1.5rem' }}>
          <h2>Tasks</h2>
          <button 
            className="btn btn-primary" 
            onClick={() => setShowForm(!showForm)}
          >
            {showForm ? 'Cancel' : '+ New Task'}
          </button>
        </div>

        {error && <div className="error">{error}</div>}

        {/* Task Form */}
        {showForm && (
          <div className="card">
            <h3>{editingTask ? 'Edit Task' : 'Create New Task'}</h3>
            <form onSubmit={handleSubmit}>
              <div className="form-group">
                <label>Title *</label>
                <input
                  type="text"
                  name="title"
                  value={formData.title}
                  onChange={handleInputChange}
                  required
                  placeholder="e.g., Build REST API"
                />
              </div>

              <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem' }}>
                <div className="form-group">
                  <label>Status</label>
                  <select name="status" value={formData.status} onChange={handleInputChange}>
                    <option value="PENDING">Pending</option>
                    <option value="IN_PROGRESS">In Progress</option>
                    <option value="DONE">Done</option>
                  </select>
                </div>

                <div className="form-group">
                  <label>Priority</label>
                  <select name="priority" value={formData.priority} onChange={handleInputChange}>
                    <option value="LOW">Low</option>
                    <option value="MEDIUM">Medium</option>
                    <option value="HIGH">High</option>
                  </select>
                </div>
              </div>

              <div className="form-group">
                <label>Category</label>
                <input
                  type="text"
                  name="category"
                  value={formData.category}
                  onChange={handleInputChange}
                  placeholder="e.g., Backend, Frontend, Database, Testing"
                />
              </div>

              <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem' }}>
                <div className="form-group">
                  <label>Estimated Time (minutes)</label>
                  <input
                    type="number"
                    name="estimatedMinutes"
                    value={formData.estimatedMinutes}
                    onChange={handleInputChange}
                    min="0"
                    placeholder="e.g., 120"
                  />
                </div>

                <div className="form-group">
                  <label>Actual Time (minutes)</label>
                  <input
                    type="number"
                    name="actualMinutes"
                    value={formData.actualMinutes}
                    onChange={handleInputChange}
                    min="0"
                    placeholder="e.g., 150"
                  />
                </div>
              </div>

              <div style={{ display: 'flex', gap: '1rem' }}>
                <button type="submit" className="btn btn-primary">
                  {editingTask ? 'Update Task' : 'Create Task'}
                </button>
                <button type="button" className="btn btn-secondary" onClick={resetForm}>
                  Cancel
                </button>
              </div>
            </form>
          </div>
        )}

        {/* Tasks List */}
        <div className="card">
          <h3>All Tasks ({tasks.length})</h3>
          {tasks.length === 0 ? (
            <p style={{ color: '#6b7280', textAlign: 'center', padding: '2rem' }}>
              No tasks yet. Create your first task to get started!
            </p>
          ) : (
            <table className="table">
              <thead>
                <tr>
                  <th>Title</th>
                  <th>Status</th>
                  <th>Priority</th>
                  <th>Category</th>
                  <th>Est. Time</th>
                  <th>Actual Time</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {tasks.map((task) => (
                  <tr key={task.id}>
                    <td style={{ fontWeight: 500 }}>{task.title}</td>
                    <td>
                      <span className={`badge badge-${task.status.toLowerCase().replace('_', '-')}`}>
                        {task.status.replace('_', ' ')}
                      </span>
                    </td>
                    <td>
                      <span className={`badge badge-${task.priority?.toLowerCase() || 'medium'}`}>
                        {task.priority || 'MEDIUM'}
                      </span>
                    </td>
                    <td>{task.category || '-'}</td>
                    <td>{task.estimatedMinutes ? `${task.estimatedMinutes} min` : '-'}</td>
                    <td>{task.actualMinutes ? `${task.actualMinutes} min` : '-'}</td>
                    <td>
                      <button 
                        className="btn btn-secondary" 
                        onClick={() => handleEdit(task)}
                        style={{ marginRight: '0.5rem', padding: '0.375rem 0.75rem' }}
                      >
                        Edit
                      </button>
                      <button 
                        className="btn btn-danger" 
                        onClick={() => handleDelete(task.id)}
                        style={{ padding: '0.375rem 0.75rem' }}
                      >
                        Delete
                      </button>
                    </td>
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

export default Tasks;
