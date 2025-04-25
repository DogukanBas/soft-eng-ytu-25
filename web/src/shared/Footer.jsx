import "./Footer.css";
import AccountBalanceWalletIcon from '@mui/icons-material/AccountBalanceWallet';
import GithubIcon from '@mui/icons-material/GitHub';

function Footer() {
    return (
        <footer className="footer-container">
            <div className="footer-content">
                <div className="footer-company">
                    <AccountBalanceWalletIcon className="footer-company-logo" />
                    <div>
                        <h3>CostMS</h3>
                        <p>Simplify your cost management</p>
                    </div>
                </div>
            </div>
            
            <div className="footer-bottom">
                <p>&copy; 2025 CostMS</p>
                
                <div className="social-icons">
                    <a href="https://github.com/" target="_blank" rel="noopener noreferrer">
                        <GithubIcon fontSize="small" /> Github
                    </a>
                </div>
            </div>
        </footer>
    );
}

export default Footer;