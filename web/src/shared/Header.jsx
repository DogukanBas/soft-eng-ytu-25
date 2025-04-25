import "./Header.css";
import AccountBalanceWalletIcon from '@mui/icons-material/AccountBalanceWallet';

function Header() {
    return (
        <header className="header-container">
            <div className="logo">
                <AccountBalanceWalletIcon className="logo-icon" />
                CostMS
            </div>
        </header>
    );
}

export default Header;