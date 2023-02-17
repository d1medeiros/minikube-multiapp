package model

type Account struct {
	Id         string      `json:"id"`
	CustomerId string      `json:"customer_id"`
	Allowed    bool        `json:"allowed"`
	Items      []OfferItem `json:"items"`
}
