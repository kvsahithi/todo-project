set -e

# Check if Tomcat is running and stop it
if ps -ef | grep -q 'apache-tomcat'; then
    echo "Shutting down Tomcat"
    ./"apache-tomcat-10.1.29 2"/bin/shutdown.sh
fi

# Remove old .war and directory if they exist
echo "Removing old .war and directory"
if [ -f ./"apache-tomcat-10.1.29 2"/webapps/backend.war ]; then
    rm ./"apache-tomcat-10.1.29 2"/webapps/backend.war
fi
if [ -d ./"apache-tomcat-10.1.29 2"/webapps/backend ]; then
    rm -rf ./"apache-tomcat-10.1.29 2"/webapps/backend
fi

# Build the Maven project
echo "Changing to backend directory and running Maven build"
cd ./backend && mvn clean package

# Copy the new .war file to the Tomcat webapps directory
echo "Copying new .war file to Tomcat"
cp ./target/backend.war ../"apache-tomcat-10.1.29 2"/webapps

# Start Tomcat
echo "Starting Tomcat"
 ../"apache-tomcat-10.1.29 2"/bin/startup.sh
