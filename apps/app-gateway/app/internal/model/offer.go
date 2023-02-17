package model

type Offer struct {
	AccountId string      `json:"account_id"`
	Items     []OfferItem `json:"items"`
}

type OfferItem struct {
	Name  string
	Value float64
}
