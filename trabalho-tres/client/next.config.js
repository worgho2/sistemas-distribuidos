/** @type {import('next').NextConfig} */
const nextConfig = {
    reactStrictMode: false,
    swcMinify: true,
    images: {
        remotePatterns: [
            {
                protocol: 'http',
                hostname: '**',
                pathname: '**',
            },
            {
                protocol: 'https',
                hostname: '**',
                pathname: '**',
            },
        ],
    },
    output: 'standalone',
};

module.exports = nextConfig;
