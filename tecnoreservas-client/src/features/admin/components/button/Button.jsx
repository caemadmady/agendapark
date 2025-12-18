import './style.css';

const Button = ({ onClick, children, type = 'button', className = '' }) => {
  return (
    <button type={type} className={`custom-button ${className}`} onClick={onClick}>
      {children}
    </button>
  );
};

export default Button;