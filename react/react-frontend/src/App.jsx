import {Spinner, Stack, Text, Wrap, WrapItem} from "@chakra-ui/react";
import SidebarWithHeader from "./components/shared/SideBar.jsx";
import {useEffect, useState} from "react";
import {getCustomers} from "./services/client.js";
import CardWithImage from "./components/Card.jsx";
import CreateCustomerDrawer from "./components/CreateCustomerDrawer.jsx";
import {errorNotification} from "./services/notification.js";


const App = () => {

    const [customers, setCustomers] = useState([])
    const [loading, setLoading] = useState(false)
    const [error, setError] = useState("");

    const fetchCustomers = () => {
        setLoading(true)
        setTimeout(() => {
            getCustomers().then(res => {
                setCustomers(res.data)
            }).catch(err => {
                setError(err.response.data.message)
                console.log(err)
                errorNotification(
                    err.code,
                    err.response.data.message
                )
            }).finally(() => {
                setLoading(false)
            })
        }, 2000)
    }

    useEffect(() => {
        fetchCustomers();
    }, [])

    if (loading) {
        return (
            <SidebarWithHeader>
                <Stack direction={'row'}>
                    <Text fontSize={25}>Customers Loading</Text>
                    <Spinner
                        thickness='4px'
                        speed='0.65s'
                        emptyColor='gray.200'
                        color='blue.500'
                        size='xl'
                    />
                </Stack>
            </SidebarWithHeader>
        )
    }

    if (error) {
        return (
            <SidebarWithHeader>
                <CreateCustomerDrawer
                    fetchCustomers={fetchCustomers}
                />
                <Text mt={5}>OOPS something went wrong</Text>
            </SidebarWithHeader>
        )
    }

    if (customers.length <= 0) {
        return (
            <SidebarWithHeader>
                <CreateCustomerDrawer
                    fetchCustomers={fetchCustomers}
                />
               <Text mt={5}>No Customers Available</Text>
            </SidebarWithHeader>
        )
    }

    return (
        <SidebarWithHeader>
            <CreateCustomerDrawer
                fetchCustomers={fetchCustomers}
            />
            <Wrap spacing={"30px"} justify={"center"}>
                {customers.map((customer, index) => (
                    <WrapItem key={index}>
                        <CardWithImage
                            id = {customer.id}
                            name={customer.name}
                            email={customer.email}
                            age={customer.age}
                            gender={customer.gender}
                            password={customer.password}
                            imageNumber={index}
                            fetchCustomers={fetchCustomers}
                        />
                    </WrapItem>
                ))}
            </Wrap>
        </SidebarWithHeader>
    )
}

export default App;